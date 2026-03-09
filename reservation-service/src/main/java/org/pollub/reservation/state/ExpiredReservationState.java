package org.pollub.reservation.state;

//L6 State Design Pattern - Concrete State for EXPIRED reservation status
/**
 * State implementation for EXPIRED reservation status.
 * Terminal state - reservation has expired.
 */
public class ExpiredReservationState implements ReservationState {

    @Override
    public boolean canFulfill() {
        return false;
    }

    @Override
    public boolean canCancel() {
        return false;
    }

    @Override
    public boolean canExpire() {
        return false;
    }

    @Override
    public void validateForFulfillment() {
        throw new IllegalStateException("Cannot fulfill a reservation that is expired");
    }

    @Override
    public void validateForCancellation() {
        throw new IllegalStateException("Cannot cancel a reservation that is expired");
    }

    @Override
    public void validateForExpiration() {
        throw new IllegalStateException("Cannot expire a reservation that is already expired");
    }

    @Override
    public String getStateName() {
        return "EXPIRED";
    }
}

