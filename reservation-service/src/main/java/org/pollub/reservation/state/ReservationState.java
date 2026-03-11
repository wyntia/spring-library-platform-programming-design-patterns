package org.pollub.reservation.state;

//start L3 State Design Pattern - State interface for Reservation Status
/**
 * State interface for Reservation Status state machine.
 * Represents different states of a reservation (ACTIVE, FULFILLED, CANCELLED, EXPIRED).
 */
public interface ReservationState {

    /**
     * Check if reservation can be fulfilled from this state
     */
    boolean canFulfill();

    /**
     * Check if reservation can be cancelled from this state
     */
    boolean canCancel();

    /**
     * Check if reservation can expire from this state
     */
    boolean canExpire();

    /**
     * Validate if operation is allowed from this state
     */
    void validateForFulfillment();

    void validateForCancellation();

    void validateForExpiration();

    /**
     * Get the string representation of the state
     */
    String getStateName();
}

