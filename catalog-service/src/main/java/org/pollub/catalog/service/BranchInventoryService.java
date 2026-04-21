package org.pollub.catalog.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pollub.catalog.client.ReservationServiceClient;
import org.pollub.catalog.model.BranchInventory;
import org.pollub.catalog.model.CopyStatus;
import org.pollub.catalog.model.dto.BranchInventoryDto;
import org.pollub.catalog.model.dto.ReservationCatalogRequestDto;
import org.pollub.catalog.repository.IBranchInventoryRepository;
import org.pollub.common.Observer;
import org.pollub.common.Subject;
import org.pollub.common.config.DateTimeProvider;
import org.pollub.common.dto.RentalHistoryDto;
import org.pollub.common.dto.ReservationResponse;
import org.pollub.common.event.BranchInventoryEvent;
import org.pollub.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing per-branch inventory of library items.
 * Implements Observer pattern to notify observers about inventory state
 * changes.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BranchInventoryService implements IBranchInventoryService, Subject {

    private final IBranchInventoryRepository inventoryRepository;
    private final ReservationServiceClient reservationServiceClient;
    // Lab4 OCP Start
    private final InventoryTransitionPolicy inventoryTransitionPolicy;
    // Lab4 OCP End
    private final List<Observer> observers = new ArrayList<>();

    // Lab6 : Długość metod 3 Start
    // Lab6 : Maksymalnie 3 argumenty 1 Start
    private record RentCopyParties(Long itemId, Long branchId, Long userId) {
    }

    private record PendingRentPersistence(
            BranchInventory inventory,
            RentCopyParties parties,
            boolean wasReserved,
            RentalHistoryDto rentalHistoryDto) {
    }

    @Override
    public ReservationResponse rentCopy(Long itemId, RentalHistoryDto rentalHistoryDto) {
        Long branchId = rentalHistoryDto.getBranchId();
        Long userId = rentalHistoryDto.getUserId();
        BranchInventory inventory = getBranchInventoryOrThrow(itemId, branchId);
        validateReservationOwnership(inventory, userId);
        validateAvailabilityForRent(inventory);
        // Check if the book was reserved before we clear the reservation info
        boolean wasReserved = inventory.getStatus() == CopyStatus.RESERVED;
        RentCopyParties parties = new RentCopyParties(itemId, branchId, userId);
        applyRentDataAndClearReservation(inventory, rentalHistoryDto, parties);
        return persistRentedCopyAndBuildResponse(
                new PendingRentPersistence(inventory, parties, wasReserved, rentalHistoryDto));
    }

    private void applyRentDataAndClearReservation(
            BranchInventory inventory, RentalHistoryDto rentalHistoryDto, RentCopyParties parties) {
        updateInventoryRecordWithRentData(
                inventory,
                inventoryTransitionPolicy.resolveTargetStatus(InventoryOperation.RENT),
                new RentAssignmentDetails(
                        parties.userId(), rentalHistoryDto.getRentedAt(), rentalHistoryDto.getDueDate()));
        clearReservationInfo(inventory);
    }

    private ReservationResponse persistRentedCopyAndBuildResponse(PendingRentPersistence pending) {
        try {
            inventoryRepository.save(pending.inventory());
            notifyRentObservers(pending.parties());
            fulfillReservationIfWasReserved(pending.wasReserved(), pending.parties());
            return buildRentReservationResponse(pending.parties(), pending.rentalHistoryDto());
        } catch (Exception e) {
            log.error(
                    "Error renting copy of item {} at branch {}: {}",
                    pending.parties().itemId(),
                    pending.parties().branchId(),
                    e.getMessage());
            throw e;
        }
    }

    private void notifyRentObservers(RentCopyParties parties) {
        // Notify observers about rent
        notifyObservers(new BranchInventoryEvent(
                "RENT",
                parties.itemId(),
                parties.branchId(),
                parties.userId(),
                DateTimeProvider.getInstance().now()));
    }

    private void fulfillReservationIfWasReserved(boolean wasReserved, RentCopyParties parties) {
        // If the book was reserved, mark the reservation as fulfilled in
        // reservation-service
        if (wasReserved) {
            reservationServiceClient.fulfillReservation(
                    parties.itemId(), parties.branchId(), parties.userId());
        }
    }

    private static ReservationResponse buildRentReservationResponse(
            RentCopyParties parties, RentalHistoryDto rentalHistoryDto) {
        return ReservationResponse.builder()
                .itemId(parties.itemId())
                .branchId(parties.branchId())
                .userId(parties.userId())
                .rentedAt(rentalHistoryDto.getRentedAt())
                .dueDate(rentalHistoryDto.getDueDate())
                .status(CopyStatus.RENTED.name())
                .build();
    }
    // Lab6 : Maksymalnie 3 argumenty 1 Stop
    // Lab6 : Długość metod 3 Stop

    private static void clearReservationInfo(BranchInventory inventory) {
        inventory.setReservedByUserId(null);
        inventory.setReservedAt(null);
        inventory.setReservationExpiresAt(null);
    }

    // Lab6 : Maksymalnie 3 argumenty 2 Start
    private record RentAssignmentDetails(Long userId, LocalDateTime rentedAt, LocalDateTime dueDate) {
    }

    private static void updateInventoryRecordWithRentData(
            BranchInventory inventory, CopyStatus rented, RentAssignmentDetails assignment) {
        inventory.setStatus(rented);
        inventory.setRentedByUserId(assignment.userId());
        inventory.setRentedAt(assignment.rentedAt());
        inventory.setDueDate(assignment.dueDate());
        inventory.setRentExtended(false);
    }
    // Lab6 : Maksymalnie 3 argumenty 2 Stop

    private BranchInventory getBranchInventoryOrThrow(Long itemId, Long branchId) {
        BranchInventory inventory = inventoryRepository.findByItemIdAndBranchId(itemId, branchId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No copy of item " + itemId + " found at branch " + branchId));
        return inventory;
    }

    private void validateReservationOwnership(BranchInventory inventory, Long userId) {
        if (inventory.getStatus() == CopyStatus.RESERVED &&
                !userId.equals(inventory.getReservedByUserId())) {

            throw new IllegalStateException(
                    "Copy is reserved by another user. Reserved by userId: "
                            + inventory.getReservedByUserId());
        }
    }

    private void validateAvailabilityForRent(BranchInventory inventory) {
        // Lab4 OCP Start
        inventoryTransitionPolicy.throwIfOperationNotAllowed(
                inventory.getStatus(),
                InventoryOperation.RENT,
                "Copy is not available for rent. Current status: ");
        // Lab4 OCP End
    }

    @Override
    public void returnCopy(Long itemId, Long branchId) {
        log.info("Returning item {} to branch {}", itemId, branchId);

        BranchInventory inventory = getBranchInventoryOrThrow(itemId, branchId);

        inventoryTransitionPolicy.throwIfOperationNotAllowed(
                inventory.getStatus(),
                InventoryOperation.RETURN,
                "Copy is not rented. Current status: ");

        Long rentedByUserId = inventory.getRentedByUserId();
        updateInventoryRecordWithRentData(
                inventory,
                inventoryTransitionPolicy.resolveTargetStatus(InventoryOperation.RETURN),
                new RentAssignmentDetails(null, null, null));

        try {
            inventoryRepository.save(inventory);

            // Notify observers about return
            notifyObservers(new BranchInventoryEvent(
                    "RETURN",
                    itemId,
                    branchId,
                    rentedByUserId,
                    DateTimeProvider.getInstance().now()));
        } catch (Exception e) {
            log.error("Error returning copy of item {} at branch {}: {}", itemId, branchId, e.getMessage());
            throw e;
        }
    }

    @Override
    public BranchInventoryDto reserveCopy(Long itemId, ReservationCatalogRequestDto reservationCatalogRequestDto) {
        Long branchId = reservationCatalogRequestDto.getBranchId();

        BranchInventory inventory = getBranchInventoryOrThrow(itemId, branchId);

        inventoryTransitionPolicy.throwIfOperationNotAllowed(
                inventory.getStatus(),
                InventoryOperation.RESERVE,
                "Copy is not available for reservation. Current status: ");

        inventory.setStatus(inventoryTransitionPolicy.resolveTargetStatus(InventoryOperation.RESERVE));
        Long userId = reservationCatalogRequestDto.getUserId();
        inventory.setReservedByUserId(userId);
        inventory.setReservedAt(DateTimeProvider.getInstance().now());
        inventory.setReservationExpiresAt(
                reservationCatalogRequestDto.getExpiresAt());

        try {
            BranchInventory savedInventory = inventoryRepository.save(inventory);

            // Notify observers about reservation
            notifyObservers(new BranchInventoryEvent(
                    "RESERVE",
                    itemId,
                    branchId,
                    userId,
                    DateTimeProvider.getInstance().now()));

            return toDto(savedInventory);
        } catch (Exception e) {
            log.error("Error reserving copy of item {} at branch {}: {}", itemId, branchId, e.getMessage());
            throw e;
        }
    }

    @Override
    public BranchInventory cancelReservation(Long itemId, Long branchId) {
        log.info("Cancelling reservation for item {} at branch {}", itemId, branchId);

        BranchInventory inventory = getBranchInventoryOrThrow(itemId, branchId);

        inventoryTransitionPolicy.throwIfOperationNotAllowed(
                inventory.getStatus(),
                InventoryOperation.CANCEL_RESERVATION,
                "Copy is not reserved. Current status: ");

        Long reservedByUserId = inventory.getReservedByUserId();
        inventory.setStatus(inventoryTransitionPolicy.resolveTargetStatus(InventoryOperation.CANCEL_RESERVATION));
        clearReservationInfo(inventory);

        BranchInventory savedInventory = inventoryRepository.save(inventory);

        // Notify observers about reservation cancellation
        notifyObservers(new BranchInventoryEvent(
                "CANCEL_RESERVATION",
                itemId,
                branchId,
                reservedByUserId,
                DateTimeProvider.getInstance().now()));

        return savedInventory;
    }

    // L6 Use State Pattern validation for rental extension
    private void throwIfNotRented(BranchInventory inventory) {
        inventoryTransitionPolicy.throwIfOperationNotAllowed(
                inventory.getStatus(),
                InventoryOperation.EXTEND,
                "Copy is not rented. Current status: ");
    }

    @Override
    @Transactional
    public void extendRental(Long itemId, Long branchId, int additionalDays) {
        log.info("Extending rental for item {} at branch {} by {} days", itemId, branchId, additionalDays);

        BranchInventory inventory = getBranchInventoryOrThrow(itemId, branchId);

        throwIfNotRented(inventory);

        throwIfHaveAlreadyBeenExtended(inventory);

        LocalDateTime newDueDate = inventory.getDueDate() != null
                ? inventory.getDueDate().plusDays(additionalDays)
                : DateTimeProvider.getInstance().now().plusDays(additionalDays);

        inventory.setDueDate(newDueDate);
        inventory.setRentExtended(true);

        try {
            inventoryRepository.save(inventory);

            // Notify observers about rental extension
            notifyObservers(new BranchInventoryEvent(
                    "EXTEND",
                    itemId,
                    branchId,
                    inventory.getRentedByUserId(),
                    DateTimeProvider.getInstance().now()));
        } catch (Exception e) {
            log.error("Error extending rental for item {} at branch {}: {}", itemId, branchId, e.getMessage());
            throw e;
        }
    }

    private static void throwIfHaveAlreadyBeenExtended(BranchInventory inventory) {
        if (Boolean.TRUE.equals(inventory.getRentExtended())) {
            throw new IllegalStateException("Rental has already been extended once.");
        }
    }

    @Override
    public List<BranchInventoryDto> getInventoryForItem(Long itemId) {
        return inventoryRepository.findByItemId(itemId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<BranchInventory> getAvailableCopies(Long itemId) {
        return inventoryRepository.findByItemId(itemId).stream()
                .filter(inv -> inv.getStatus() == CopyStatus.AVAILABLE)
                .toList();
    }

    @Override
    public List<Long> getAvailableItemsAtBranch(Long branchId) {
        return inventoryRepository.findItemIdsByBranchIdAndStatus(branchId, CopyStatus.AVAILABLE);
    }

    @Override
    public boolean isAvailableAtBranch(Long itemId, Long branchId) {
        return inventoryRepository.existsByItemIdAndBranchIdAndStatus(itemId, branchId, CopyStatus.AVAILABLE);
    }

    @Override
    public List<Long> getAvailableBranchIds(Long itemId) {
        return inventoryRepository.findAvailableBranchIds(itemId);
    }

    @Override
    public List<BranchInventory> getRentedByUser(Long userId) {
        return inventoryRepository.findByRentedByUserId(userId);
    }

    @Override
    @Transactional
    public BranchInventory addInventory(Long itemId, Long branchId) {
        log.info("Adding inventory for item {} at branch {}", itemId, branchId);

        if (inventoryRepository.existsByItemIdAndBranchId(itemId, branchId)) {
            throw new IllegalStateException("Inventory already exists for item " + itemId + " at branch " + branchId);
        }

        BranchInventory inventory = BranchInventory.builder()
                .itemId(itemId)
                .branchId(branchId)
                .status(CopyStatus.AVAILABLE)
                .build();

        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public void updateStatus(Long itemId, Long branchId, String statusStr) {
        log.info("Updating status for item {} at branch {} to {}", itemId, branchId, statusStr);

        BranchInventory inventory = getBranchInventoryOrThrow(itemId, branchId);

        CopyStatus status;
        try {
            status = CopyStatus.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + statusStr);
        }

        inventory.setStatus(status);
        inventoryRepository.save(inventory);

        // Notify observers about status change
        notifyObservers(new BranchInventoryEvent(
                "STATUS_CHANGED",
                itemId,
                branchId,
                null,
                DateTimeProvider.getInstance().now()));
    }

    private BranchInventoryDto toDto(BranchInventory inventory) {
        return BranchInventoryDto.builder()
                .id(inventory.getId())
                .itemId(inventory.getItemId())
                .branchId(inventory.getBranchId())
                .status(inventory.getStatus().name())
                .rentedByUserId(inventory.getRentedByUserId())
                .rentedAt(inventory.getRentedAt())
                .dueDate(inventory.getDueDate())
                .rentExtended(inventory.getRentExtended())
                .reservedByUserId(inventory.getReservedByUserId())
                .reservedAt(inventory.getReservedAt())
                .reservationExpiresAt(inventory.getReservationExpiresAt())
                .build();
    }

    // Observer pattern implementation

    @Override
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            log.debug("Observer attached: {}", observer.getClass().getSimpleName());
        }
    }

    @Override
    public void detach(Observer observer) {
        if (observers.remove(observer)) {
            log.debug("Observer detached: {}", observer.getClass().getSimpleName());
        }
    }

    @Override
    public void notifyObservers(Object event) {
        for (Observer observer : observers) {
            observer.update(this, event);
        }
    }
}
