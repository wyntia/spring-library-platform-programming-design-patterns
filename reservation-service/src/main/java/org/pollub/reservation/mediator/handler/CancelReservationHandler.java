package org.pollub.reservation.mediator.handler;

import lombok.RequiredArgsConstructor;
import org.pollub.common.config.DateTimeProvider;
import org.pollub.common.event.ReservationEvent;
import org.pollub.common.mediator.Mediator;
import org.pollub.common.mediator.RequestHandler;
import org.pollub.reservation.mediator.request.CancelReservationRequest;
import org.pollub.reservation.mediator.request.UpdateCatalogStatusRequest;
import org.pollub.reservation.model.ReservationHistory;
import org.pollub.reservation.model.ReservationStatus;
import org.pollub.reservation.repository.ReservationRepository;
import org.pollub.reservation.service.ReservationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//Lab5 Mediator Start
@Component
@RequiredArgsConstructor
public class CancelReservationHandler implements RequestHandler<CancelReservationRequest, Void> {

    private final ReservationRepository reservationRepository;
    @Lazy
    private final Mediator mediator;
    private final ReservationService reservationService;

    @Override
    @Transactional
    public Void handle(CancelReservationRequest request) {
        ReservationHistory reservation = reservationRepository.findById(request.id()).orElseThrow(() ->
                new IllegalStateException("Reservation not found: " + request.id())
        );
        if (!reservation.getUserId().equals(request.userId())) {
            throw new RuntimeException(
                    "User " + request.userId() + " is not authorized to cancel reservation " + request.id()
            );
        }

        //L6 Use State Pattern validation
        reservation.getState().validateForCancellation();

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setResolvedAt(DateTimeProvider.getInstance().now());
        reservationRepository.save(reservation);

        //L6 Notify observers about reservation cancellation
        reservationService.notifyObservers(new ReservationEvent(
            "CANCELLED",
            reservation.getId(),
            reservation.getItemId(),
            request.userId(),
            DateTimeProvider.getInstance().now()
        ));

        mediator.send(new UpdateCatalogStatusRequest(
                reservation.getItemId(),
                reservation.getBranchId(),
                "AVAILABLE"
        ));

        return null;
    }
}
//Lab5 Mediator End
