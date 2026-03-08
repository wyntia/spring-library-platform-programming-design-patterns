package org.pollub.reservation.command;

import org.pollub.reservation.model.ReservationHistory;
import org.pollub.reservation.model.ReservationStatus;
import org.pollub.reservation.repository.ReservationRepository;
import org.pollub.reservation.memento.ReservationMemento;
import org.pollub.common.config.DateTimeProvider;
import java.nio.file.AccessDeniedException;

//start L5 Command
public class CancelReservationCommand {
    private final ReservationRepository reservationRepository;
    private final Long id;
    private final Long userId;
    private ReservationMemento memento; //obiekt Memento

    public CancelReservationCommand(ReservationRepository reservationRepository, Long id, Long userId) {
        this.reservationRepository = reservationRepository;
        this.id = id;
        this.userId = userId;
    }

    public void execute() throws AccessDeniedException {
        ReservationHistory reservation = reservationRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Reservation not found: " + id)
        );
        if (!reservation.getUserId().equals(userId)) {
            throw new AccessDeniedException(
                    "User " + userId + " is not authorized to cancel reservation " + id
            );
        }
        //start L5 Memento
        this.memento = new ReservationMemento(reservation);
        //end L5 Memento
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setResolvedAt(DateTimeProvider.getInstance().now());
        reservationRepository.save(reservation);
    }

    //start L5 Memento
    public ReservationMemento getMemento() {
        return memento;
    }
    //end L5 Memento
}
//end L5 Command
