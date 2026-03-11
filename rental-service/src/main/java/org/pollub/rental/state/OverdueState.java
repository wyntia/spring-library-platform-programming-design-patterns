package org.pollub.rental.state;

//L3 State Design Pattern - Concrete State for OVERDUE rental status
/**
 * State implementation for OVERDUE rental status.
 * Rental can be returned but cannot be extended when overdue.
 */
public class OverdueState implements RentalState {

    @Override
    public boolean canReturn() {
        return true;
    }

    @Override
    public boolean canExtend() {
        return false;
    }

    @Override
    public boolean isOverdue() {
        return true;
    }

    @Override
    public void validateForReturn() {
        // Overdue item can be returned - no validation error
    }

    @Override
    public void validateForExtension() {
        throw new IllegalStateException("Cannot extend an overdue rental");
    }

    @Override
    public String getStateName() {
        return "OVERDUE";
    }
}

