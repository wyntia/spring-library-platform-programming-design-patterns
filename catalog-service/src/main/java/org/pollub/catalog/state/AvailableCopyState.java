package org.pollub.catalog.state;

//L6 State Implementation for Copy Status - AVAILABLE state
/**
 * State implementation for AVAILABLE copy status.
 * Copy can be rented or reserved when available.
 */
public class AvailableCopyState implements CopyState {

    @Override
    public boolean canRent() {
        return true;
    }

    @Override
    public boolean canReserve() {
        return true;
    }

    @Override
    public boolean canReturn() {
        return false;
    }

    @Override
    public boolean canExtend() {
        return false;
    }

    @Override
    public void validateForRent() {
        // Available copy can be rented - no validation error
    }

    @Override
    public void validateForReservation() {
        // Available copy can be reserved - no validation error
    }

    @Override
    public void validateForReturn() {
        throw new IllegalStateException("Cannot return a copy that is not rented");
    }

    @Override
    public void validateForExtension() {
        throw new IllegalStateException("Cannot extend rental for a copy that is not rented");
    }

    @Override
    public String getStateName() {
        return "AVAILABLE";
    }
}

