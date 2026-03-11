package org.pollub.catalog.state;

//start L3 State Design Pattern - State interface for Copy Status
/**
 * State interface for Copy Status state machine.
 * Represents different states of a book copy (AVAILABLE, RENTED, RESERVED).
 */
public interface CopyState {

    /**
     * Check if copy can be rented from this state
     */
    boolean canRent();

    /**
     * Check if copy can be reserved from this state
     */
    boolean canReserve();

    /**
     * Check if copy can be returned from this state
     */
    boolean canReturn();

    /**
     * Check if rental can be extended from this state
     */
    boolean canExtend();

    /**
     * Validate if operation is allowed from this state
     */
    void validateForRent();

    void validateForReservation();

    void validateForReturn();

    void validateForExtension();

    /**
     * Get the string representation of the state
     */
    String getStateName();
}
//end L3 State Design Pattern - State interface for Copy Status
