package org.pollub.rental.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pollub.common.Observer;
import org.pollub.common.Subject;
import org.pollub.common.config.DateTimeProvider;
import org.pollub.common.dto.ItemDto;
import org.pollub.common.dto.RentalHistoryDto;
import org.pollub.common.dto.ReservationResponse;
import org.pollub.common.event.RentalEvent;
import org.pollub.common.mediator.Mediator;
import org.pollub.rental.bridge.IValidationBridge;
import org.pollub.rental.mediator.request.ExtendRentalRequest;
import org.pollub.rental.mediator.request.GetActiveRentalsRequest;
import org.pollub.rental.mediator.request.MarkAsRentedRequest;
import org.pollub.rental.mediator.request.MarkAsReturnedRequest;
import org.pollub.rental.mediator.request.SendRentalConfirmationNotification;
import org.pollub.rental.mediator.request.SendReturnConfirmationNotification;
import org.pollub.rental.model.RentalHistory;
import org.pollub.rental.model.RentalStatus;
import org.pollub.rental.repository.IRentalHistoryRepository;
import org.pollub.rental.utils.IRentalValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RentalService implements IRentalService, Subject {
    private static final int DAYS_TO_RENT = 14;

    private final IRentalHistoryRepository rentalHistoryRepository;
    private final IRentalValidator rentalValidator;
    private final IValidationBridge validationBridge;
    //Lab5 Mediator Start
    private final Mediator mediator;
    //Lab5 Mediator End
    private final List<Observer> observers = new ArrayList<>();

    public List<ItemDto> getActiveRentals(Long userId) {
        return mediator.send(new GetActiveRentalsRequest(userId));
    }

    public List<RentalHistory> getUserRentalHistory(Long userId) {
        return rentalHistoryRepository.findByUserIdOrderByReturnedAtDesc(userId);
    }

    public List<RentalHistory> getItemRentalHistory(Long itemId) {
        return rentalHistoryRepository.findByItemId(itemId);
    }

    @Override
    @Transactional
    public ReservationResponse rentItem(Long itemId, Long userId, Long branchId) {
//        this.rentalValidator.validateAbilityToRentOrThrow(userId, itemId);
        validationBridge.validateAbilityToRentOrThrow(userId, itemId);

        RentalHistory rentalHistory = RentalHistory.builder()
                .itemId(itemId)
                .userId(userId)
                .branchId(branchId)
                .status(RentalStatus.RENTED)
                .rentedAt(DateTimeProvider.getInstance().now())
                .isExtended(false)
                .dueDate(DateTimeProvider.getInstance().now().plusDays(DAYS_TO_RENT))
                .build();
        
        try{
            rentalHistoryRepository.save(rentalHistory);
            log.info("Rental history saved: {}", rentalHistory);

            // Notify observers about rental creation
            notifyObservers(new RentalEvent(
                "CREATED",
                rentalHistory.getId(),
                itemId,
                userId,
                DateTimeProvider.getInstance().now()
            ));
        } catch (Exception e){
            log.error("Error saving rental history for itemId: {}, userId: {}. Error: {}", itemId, userId, e.getMessage());
            throw e;
        }
        //Lab5 Mediator Start
        try {
            mediator.send(new SendRentalConfirmationNotification(
                    userId, itemId, rentalHistory.getDueDate()
            ));
        } catch (Exception e) {
            log.warn("Failed to send rental confirmation notification: {}", e.getMessage());
        }
        //Lab5 Mediator End
        return mediator.send(new MarkAsRentedRequest(
                toRentalCatalogRequestDto(rentalHistory)
        ));
    }

    @Override
    public void returnItem(Long itemId, Long branchId) {
        RentalHistory rentalHistory = getRentalHistory(itemId, branchId);

        //L6 Use State Pattern validation
        rentalHistory.getState().validateForReturn();

        rentalHistory.setReturnedAt(DateTimeProvider.getInstance().now());
        rentalHistory.setStatus(RentalStatus.RETURNED);

        try{
            rentalHistoryRepository.save(rentalHistory);

            //L6 Notify observers about item return
            notifyObservers(new RentalEvent(
                "RETURNED",
                rentalHistory.getId(),
                itemId,
                rentalHistory.getUserId(),
                DateTimeProvider.getInstance().now()
            ));
        }
        catch (Exception e){
            log.error("Error updating rental history for return of itemId: {}. Error: {}", itemId, e.getMessage());
            throw e;
        }

        mediator.send(new MarkAsReturnedRequest(itemId, branchId));

        //Lab5 Mediator Start
        try {
            mediator.send(new SendReturnConfirmationNotification(
                    rentalHistory.getUserId(), itemId
            ));
        } catch (Exception e) {
            log.warn("Failed to send return confirmation notification: {}", e.getMessage());
        }
        //Lab5 Mediator End
    }

    @Override
    public void extendLoan(Long itemId, Long branchId, int days) {
        RentalHistory rentalHistory = getRentalHistory(itemId, branchId);

        //L6 Use State Pattern validation
        rentalHistory.getState().validateForExtension();

        throwIfHaveBeenAlreadyRentedBefore(itemId, rentalHistory);

        extendLoanRecord(days, rentalHistory);
        try{
            rentalHistoryRepository.save(rentalHistory);

            // Notify observers about rental extension
            notifyObservers(new RentalEvent(
                "EXTENDED",
                rentalHistory.getId(),
                itemId,
                rentalHistory.getUserId(),
                DateTimeProvider.getInstance().now()
            ));
        }
        catch (Exception e){
            log.error("Error updating rental history for extension of itemId: {}. Error: {}", itemId, e.getMessage());
            throw e;
        }

        mediator.send(new ExtendRentalRequest(itemId, branchId, days));

    }

    private static void extendLoanRecord(int days, RentalHistory rentalHistory) {
        rentalHistory.setDueDate(
                rentalHistory.getDueDate().plusDays(days)
        );
        rentalHistory.setIsExtended(true);
    }

    private static void throwIfHaveBeenAlreadyRentedBefore(Long itemId, RentalHistory rentalHistory) {
        if (rentalHistory.getIsExtended() == true) {
            throw new IllegalStateException(
                    "Rental for itemId: " + itemId + " has already been extended."
            );
        }
    }

    private RentalHistory getRentalHistory(Long itemId, Long branchId) {
        return rentalHistoryRepository
                .findByItemIdAndBranchId(itemId, branchId)
                .orElseThrow(() -> new IllegalStateException(
                        "No active rental found for itemId: " + itemId
                ));
    }

    private RentalHistoryDto toRentalCatalogRequestDto(RentalHistory rentalHistory) {
        return RentalHistoryDto.builder()
                .id(rentalHistory.getId())
                .itemId(rentalHistory.getItemId())
                .userId(rentalHistory.getUserId())
                .branchId(rentalHistory.getBranchId())
                .rentedAt(rentalHistory.getRentedAt())
                .dueDate(rentalHistory.getDueDate())
                .returnedAt(rentalHistory.getReturnedAt())
                .build();
    }

    //L6 Observer pattern implementation

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

