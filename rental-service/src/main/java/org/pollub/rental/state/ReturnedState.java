package org.pollub.rental.state;
//L3 State Design Pattern - Concrete State for RETURNED rental status
/**
 * State implementation for RETURNED rental status.
 * Terminal state - rental has been returned.
 */
public class ReturnedState implements RentalState {

    @Override
    public boolean canReturn() {
        return false;
    }

    @Override
    public boolean canExtend() {
        return false;
    }

    @Override
    public boolean isOverdue() {
        return false;
    }

    @Override
    public void validateForReturn() {
        throw new IllegalStateException("Cannot return a rental that is already returned");
    }

    @Override
    public void validateForExtension() {
        throw new IllegalStateException("Cannot extend a rental that is already returned");
    }

    @Override
    public String getStateName() {
        return "RETURNED";
    }
}

