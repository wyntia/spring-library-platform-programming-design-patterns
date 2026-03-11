package org.pollub.reservation.state;

//L3 State Design Pattern - Concrete State for CANCELLED reservation status
/**
 * State implementation for CANCELLED reservation status.
 * Terminal state - reservation has been cancelled by user.
 */
public class CancelledReservationState implements ReservationState {

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
        throw new IllegalStateException("Cannot fulfill a reservation that is cancelled");
    }

    @Override
    public void validateForCancellation() {
        throw new IllegalStateException("Cannot cancel a reservation that is already cancelled");
    }

    @Override
    public void validateForExpiration() {
        throw new IllegalStateException("Cannot expire a reservation that is cancelled");
    }

    @Override
    public String getStateName() {
        return "CANCELLED";
    }
}

