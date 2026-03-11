package org.pollub.catalog.state;
//L3 State Implementation for Copy Status - RESERVED state
/**
 * State implementation for RESERVED copy status.
 * Copy can only be returned to available state when reserved.
 */
public class ReservedCopyState implements CopyState {

    @Override
    public boolean canRent() {
        return true; // Reserved copy can be rented by the person who reserved it
    }

    @Override
    public boolean canReserve() {
        return false;
    }

    @Override
    public boolean canReturn() {
        return false; // Cannot return a reserved copy (it wasn't rented)
    }

    @Override
    public boolean canExtend() {
        return false;
    }

    @Override
    public void validateForRent() {
        // Reserved copy can be rented (by the person who reserved it) - no validation error
    }

    @Override
    public void validateForReservation() {
        throw new IllegalStateException("Cannot reserve a copy that is already reserved");
    }

    @Override
    public void validateForReturn() {
        throw new IllegalStateException("Cannot return a copy that is reserved (not rented)");
    }

    @Override
    public void validateForExtension() {
        throw new IllegalStateException("Cannot extend rental for a reserved copy");
    }

    @Override
    public String getStateName() {
        return "RESERVED";
    }
}

