package org.pollub.reservation.memento;

import org.pollub.reservation.model.ReservationHistory;

//start L5 Memento
public class ReservationMemento {
    private final ReservationHistory reservationSnapshot;

    public ReservationMemento(ReservationHistory reservation) {
        this.reservationSnapshot = new ReservationHistory();
        this.reservationSnapshot.setId(reservation.getId());
        this.reservationSnapshot.setItemId(reservation.getItemId());
        this.reservationSnapshot.setUserId(reservation.getUserId());
        this.reservationSnapshot.setBranchId(reservation.getBranchId());
        this.reservationSnapshot.setReservedAt(reservation.getReservedAt());
        this.reservationSnapshot.setExpiresAt(reservation.getExpiresAt());
        this.reservationSnapshot.setResolvedAt(reservation.getResolvedAt());
        this.reservationSnapshot.setStatus(reservation.getStatus());
    }

    public ReservationHistory getSavedState() {
        return reservationSnapshot;
    }
}
//end L5 Memento
