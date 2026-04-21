package org.pollub.rental.state;

//start L3 State Design Pattern - State interface for Rental Status
/**
 * State interface for Rental Status state machine.
 * Represents different states of a rental (RENTED, RETURNED, OVERDUE).
 */
public interface RentalState {

    /**
     * Check if rental can be returned from this state
     */
    boolean canReturn();

    /**
     * Check if rental can be extended from this state
     */
    boolean canExtend();

    /**
     * Check if rental is overdue from this state
     */
    boolean isOverdue();

    /**
     * Validate if operation is allowed from this state
     */
    void validateForReturn();

    void validateForExtension();

    /**
     * Get the string representation of the state
     */
    String getStateName();
}
//end L3 State Design Pattern - State interface for Rental Status

