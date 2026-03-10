package org.pollub.reservation.mediator.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pollub.common.config.DateTimeProvider;
import org.pollub.common.dto.ItemDto;
import org.pollub.common.event.ReservationEvent;
import org.pollub.common.mediator.Mediator;
import org.pollub.common.mediator.RequestHandler;
import org.pollub.reservation.mediator.request.CreateReservationRequest;
import org.pollub.reservation.mediator.request.MarkCatalogAsReservedRequest;
import org.pollub.reservation.model.ReservationHistory;
import org.pollub.reservation.model.ReservationStatus;
import org.pollub.reservation.model.dto.ReservationCatalogRequestDto;
import org.pollub.reservation.repository.ReservationRepository;
import org.pollub.reservation.service.ReservationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//Lab5 Mediator Start
@Component
@RequiredArgsConstructor
@Slf4j
public class CreateReservationHandler implements RequestHandler<CreateReservationRequest, ItemDto> {

    private static final int MAX_ACTIVE_RESERVATIONS = 3;

    private final ReservationRepository reservationRepository;
    @Lazy
    private final Mediator mediator;
    private final ReservationService reservationService;

    //Lab1 - Prototype 2 Start
    private final ReservationHistory templateReservation = new ReservationHistory();
    //Lab1 End Prototype 2

    @Override
    @Transactional
    public ItemDto handle(CreateReservationRequest request) {
        Long userId = request.userId();

        long activeCount = reservationRepository.countByUserIdAndStatus(userId, ReservationStatus.ACTIVE);
        if (activeCount >= MAX_ACTIVE_RESERVATIONS) {
            throw new IllegalStateException("User has reached the maximum number of active reservations");
        }

        //Lab1 - Prototype 2 Start
        ReservationHistory reservation = templateReservation.clone();
        reservation.setItemId(request.dto().getItemId());
        reservation.setBranchId(request.dto().getBranchId());
        reservation.setUserId(userId);
        //Lab1 End Prototype 2

        try {
            reservationRepository.save(reservation);

            //L6 Notify observers about reservation creation
            reservationService.notifyObservers(new ReservationEvent(
                "CREATED",
                reservation.getId(),
                request.dto().getItemId(),
                userId,
                DateTimeProvider.getInstance().now()
            ));
        } catch (Exception e) {
            log.error("Failed to update item status in catalog service for reservation {}", reservation.getId(), e);
            throw new RuntimeException("Failed to update item status in catalog service {}", e);
        }

        return mediator.send(new MarkCatalogAsReservedRequest(
            ReservationCatalogRequestDto.builder()
                .id(reservation.getId())
                .itemId(reservation.getItemId())
                .userId(reservation.getUserId())
                .branchId(reservation.getBranchId())
                .reservedAt(reservation.getReservedAt())
                .expiresAt(reservation.getExpiresAt())
                .resolvedAt(reservation.getResolvedAt())
                .status(reservation.getStatus().name())
                .build()
        ));
    }
}
//Lab5 Mediator End
