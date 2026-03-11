package org.pollub.rental.state;

//L3 State Design Pattern - Concrete State for RENTED rental status
/**
 * State implementation for RENTED rental status.
 * Rental can be returned or extended when rented.
 */
public class RentedState implements RentalState {

    @Override
    public boolean canReturn() {
        return true;
    }

    @Override
    public boolean canExtend() {
        return true;
    }

    @Override
    public boolean isOverdue() {
        return false;
    }

    @Override
    public void validateForReturn() {
        // Rented item can be returned - no validation error
    }

    @Override
    public void validateForExtension() {
        // Rented item can be extended - no validation error
    }

    @Override
    public String getStateName() {
        return "RENTED";
    }
}

