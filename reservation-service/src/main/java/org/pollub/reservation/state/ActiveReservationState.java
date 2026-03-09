package org.pollub.reservation.state;

//L6 State Design Pattern - Concrete State for ACTIVE reservation status
/**
 * State implementation for ACTIVE reservation status.
 * Reservation can be fulfilled, cancelled, or expired when active.
 */
public class ActiveReservationState implements ReservationState {

    @Override
    public boolean canFulfill() {
        return true;
    }

    @Override
    public boolean canCancel() {
        return true;
    }

    @Override
    public boolean canExpire() {
        return true;
    }

    @Override
    public void validateForFulfillment() {
        // Active reservation can be fulfilled - no validation error
    }

    @Override
    public void validateForCancellation() {
        // Active reservation can be cancelled - no validation error
    }

    @Override
    public void validateForExpiration() {
        // Active reservation can expire - no validation error
    }

    @Override
    public String getStateName() {
        return "ACTIVE";
    }
}

