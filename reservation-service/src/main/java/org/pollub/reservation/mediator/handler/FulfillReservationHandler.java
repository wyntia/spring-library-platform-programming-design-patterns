package org.pollub.reservation.mediator.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pollub.common.config.DateTimeProvider;
import org.pollub.common.event.ReservationEvent;
import org.pollub.common.mediator.RequestHandler;
import org.pollub.reservation.mediator.request.FulfillReservationRequest;
import org.pollub.reservation.model.ReservationStatus;
import org.pollub.reservation.repository.ReservationRepository;
import org.pollub.reservation.service.ReservationService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//Lab5 Mediator Start
@Component
@RequiredArgsConstructor
@Slf4j
public class FulfillReservationHandler implements RequestHandler<FulfillReservationRequest, Void> {

    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;

    @Override
    @Transactional
    public Void handle(FulfillReservationRequest request) {
        reservationRepository.findByItemIdAndBranchIdAndUserIdAndStatus(
                request.itemId(), request.branchId(), request.userId(), ReservationStatus.ACTIVE
        ).ifPresent(reservation -> {
            reservation.setStatus(ReservationStatus.FULFILLED);
            reservation.setResolvedAt(DateTimeProvider.getInstance().now());
            reservationRepository.save(reservation);

            //L6 Notify observers about reservation fulfillment
            reservationService.notifyObservers(new ReservationEvent(
                "FULFILLED",
                reservation.getId(),
                request.itemId(),
                request.userId(),
                DateTimeProvider.getInstance().now()
            ));

            log.info("Reservation {} fulfilled for itemId: {}, branchId: {}, userId: {}",
                    reservation.getId(), request.itemId(), request.branchId(), request.userId());
        });

        return null;
    }
}
//Lab5 Mediator End
