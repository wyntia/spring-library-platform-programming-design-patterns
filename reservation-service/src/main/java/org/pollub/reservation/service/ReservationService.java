package org.pollub.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.pollub.common.Observer;
import org.pollub.common.Subject;
import org.pollub.common.config.DateTimeProvider;
import org.pollub.common.event.ReservationEvent;
import org.pollub.common.mediator.Mediator;
import org.pollub.reservation.iterator.ReservationHistoryIterator;
import org.springframework.context.annotation.Lazy;
import org.pollub.reservation.mediator.request.UpdateCatalogStatusRequest;
import org.pollub.reservation.model.ReservationHistory;
import org.pollub.reservation.model.ReservationStatus;
import org.pollub.reservation.repository.ReservationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReservationService implements IReservationService, Subject {

    private final ReservationRepository reservationRepository;
    @Lazy
    private final Mediator mediator;
    private final List<Observer> observers = new ArrayList<>();

    @Override
    @Transactional
    public void cleanupExpiredReservations() {
        List<ReservationHistory> expired = reservationRepository
            .findByExpiresAtBeforeAndStatus(DateTimeProvider.getInstance().now(), ReservationStatus.ACTIVE);
        //start L3 Iterator
        ReservationHistoryIterator iterator = new ReservationHistoryIterator(expired);
        while (iterator.hasNext()) {
            ReservationHistory reservation = iterator.next();

            //L6 Use State Pattern validation
            reservation.getState().validateForExpiration();

            reservation.setStatus(ReservationStatus.EXPIRED);
            reservation.setResolvedAt(DateTimeProvider.getInstance().now());
            reservationRepository.save(reservation);

            //L6 Notify observers about reservation expiration
            notifyObservers(new ReservationEvent(
                "EXPIRED",
                reservation.getId(),
                reservation.getItemId(),
                reservation.getUserId(),
                DateTimeProvider.getInstance().now()
            ));

            mediator.send(new UpdateCatalogStatusRequest(
                reservation.getItemId(),
                reservation.getBranchId(),
                "AVAILABLE"
            ));
        }
        //end L3 Iterator
        log.info("Processed {} expired reservations", expired.size());
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
