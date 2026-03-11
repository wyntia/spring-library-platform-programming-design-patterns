package org.pollub.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.pollub.common.Observer;
import org.pollub.common.Subject;
import org.pollub.common.config.DateTimeProvider;
import org.pollub.common.dto.ItemDto;
import org.pollub.common.dto.ReservationItemDto;
import org.pollub.common.event.ReservationEvent;
import org.pollub.reservation.client.CatalogServiceClient;
import org.pollub.reservation.interpreter.ReservationSearchExpression;
import org.pollub.reservation.interpreter.StatusReservationExpression;
import org.pollub.reservation.iterator.ReservationHistoryIterator;
import org.pollub.reservation.model.ReservationHistory;
import org.pollub.reservation.model.ReservationStatus;
import org.pollub.reservation.model.dto.ReservationCatalogRequestDto;
import org.pollub.reservation.model.dto.ReservationDto;
import org.pollub.reservation.repository.ReservationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReservationService implements IReservationService, Subject {

    private static final int MAX_ACTIVE_RESERVATIONS = 3;
    
    private final ReservationRepository reservationRepository;
    private final CatalogServiceClient catalogServiceClient;
    private final List<Observer> observers = new ArrayList<>();

    private final ReservationHistory templateReservation = new ReservationHistory();

    @Override
    @Transactional
    public ItemDto createReservation(ReservationDto dto, Long userId) {
        // Check active reservations count
        long activeCount = reservationRepository.countByUserIdAndStatus(userId, ReservationStatus.ACTIVE);
        if (activeCount >= MAX_ACTIVE_RESERVATIONS) {
            throw new IllegalStateException("User has reached the maximum number of active reservations");
        }

        //Lab1 - Prototype 2 Start
        // Create reservation from template clone with default values set in constructor
        ReservationHistory reservation = templateReservation.clone();
        reservation.setItemId(dto.getItemId());
        reservation.setBranchId(dto.getBranchId());
        reservation.setUserId(userId);
        //Lab1 End Prototype 2

        try {
            reservationRepository.save(reservation);

            //L3 Notify observers about reservation creation
            notifyObservers(new ReservationEvent(
                "CREATED",
                reservation.getId(),
                dto.getItemId(),
                userId,
                DateTimeProvider.getInstance().now()
            ));
        } catch (Exception e) {
            log.error("Failed to update item status in catalog service for reservation {}", reservation.getId(), e);
            throw new RuntimeException("Failed to update item status in catalog service {}", e);
        }
        return catalogServiceClient.markAsReserved(
                toReservationCatalogRequestDto(reservation)
        );

    }
    private ReservationCatalogRequestDto toReservationCatalogRequestDto(
            ReservationHistory reservation
    ) {
        return ReservationCatalogRequestDto.builder()
                .id(reservation.getId())
                .itemId(reservation.getItemId())
                .userId(reservation.getUserId())
                .branchId(reservation.getBranchId())
                .reservedAt(reservation.getReservedAt())
                .expiresAt(reservation.getExpiresAt())
                .resolvedAt(reservation.getResolvedAt())
                .status(reservation.getStatus().name())
                .build();

    }

    @Override
        public List<ReservationItemDto> getReservationsByUsername(Long userId) {
        List<ReservationHistory> allReservations = reservationRepository.findByUserId(userId);
        //start L5 Interpreter
        ReservationSearchExpression expr = new StatusReservationExpression(ReservationStatus.ACTIVE);
        List<ReservationHistory> reservations = expr.interpret(allReservations);
        //end L5 Interpreter

        List<Long> itemIds = reservations.stream()
            .map(ReservationHistory::getItemId)
            .distinct()
            .toList();

        List<ReservationItemDto.Item> items = catalogServiceClient.getItemsInfo(itemIds);
        return reservations.stream()
            .map(reservation -> {
                ReservationItemDto.Item item = items.stream()
                    .filter(i -> i.getId().equals(reservation.getItemId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Item info not found for itemId: " + reservation.getItemId()));
                return toReservationItemDto(reservation, item);
            })
            .toList();
        }

    @Override
    @Transactional
    public void cancelReservation(Long id, Long userId) throws AccessDeniedException {
        ReservationHistory reservation = reservationRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Reservation not found: " + id)
        );
        if (!reservation.getUserId().equals(userId)) {
            throw new AccessDeniedException(
                    "User " + userId + " is not authorized to cancel reservation " + id
            );
        }
        //L3 Use State Pattern validation
        reservation.getState().validateForCancellation();

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setResolvedAt(DateTimeProvider.getInstance().now());
        reservationRepository.save(reservation);

        //L3 Notify observers about reservation cancellation
        notifyObservers(new ReservationEvent(
            "CANCELLED",
            reservation.getId(),
            reservation.getItemId(),
            userId,
            DateTimeProvider.getInstance().now()
        ));

        catalogServiceClient.updateStatus(
                reservation.getItemId(),
                reservation.getBranchId(),
                "AVAILABLE"
        );

    }

    private ReservationItemDto toReservationItemDto(ReservationHistory reservation, ReservationItemDto.Item item) {
        ReservationItemDto.Item itemDto = ReservationItemDto.Item.builder()
                .id(item.getId())
                .title(item.getTitle())
                .imageUrl(item.getImageUrl())
                .build();

        return ReservationItemDto.builder()
                .id(reservation.getId())
                .branchId(reservation.getBranchId())
                .expiresAt(reservation.getExpiresAt())
                .item(itemDto)
                .build();
    }


    @Override
    @Transactional
    public void cleanupExpiredReservations() {
        List<ReservationHistory> expired = reservationRepository
            .findByExpiresAtBeforeAndStatus(DateTimeProvider.getInstance().now(), ReservationStatus.ACTIVE);
        //start L5 Iterator
        ReservationHistoryIterator iterator = new ReservationHistoryIterator(expired);
        while (iterator.hasNext()) {
            ReservationHistory reservation = iterator.next();

            //L3 Use State Pattern validation
            reservation.getState().validateForExpiration();

            reservation.setStatus(ReservationStatus.EXPIRED);
            reservation.setResolvedAt(DateTimeProvider.getInstance().now());
            reservationRepository.save(reservation);

            //L3 Notify observers about reservation expiration
            notifyObservers(new ReservationEvent(
                "EXPIRED",
                reservation.getId(),
                reservation.getItemId(),
                reservation.getUserId(),
                DateTimeProvider.getInstance().now()
            ));

            catalogServiceClient.updateStatus(
                reservation.getItemId(),
                reservation.getBranchId(),
                "AVAILABLE"
            );
        }
        //end L5 Iterator
        log.info("Processed {} expired reservations", expired.size());
    }

    @Override
    @Transactional
    public void fulfillReservation(Long itemId, Long branchId, Long userId) {
        reservationRepository.findByItemIdAndBranchIdAndUserIdAndStatus(itemId, branchId, userId, ReservationStatus.ACTIVE)
                .ifPresent(reservation -> {
                    reservation.setStatus(ReservationStatus.FULFILLED);
                    reservation.setResolvedAt(DateTimeProvider.getInstance().now());
                    reservationRepository.save(reservation);

                    //L3 Notify observers about reservation fulfillment
                    notifyObservers(new ReservationEvent(
                        "FULFILLED",
                        reservation.getId(),
                        itemId,
                        userId,
                        DateTimeProvider.getInstance().now()
                    ));

                    log.info("Reservation {} fulfilled for itemId: {}, branchId: {}, userId: {}",
                            reservation.getId(), itemId, branchId, userId);
                });
    }

    //L3 Observer pattern implementation

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
