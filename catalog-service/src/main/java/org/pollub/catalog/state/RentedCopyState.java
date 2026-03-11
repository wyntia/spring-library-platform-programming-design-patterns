package org.pollub.catalog.state;
//L3 State Implementation for Copy Status - RENTED state
/**
 * State implementation for RENTED copy status.
 * Copy can be returned or rental extended when rented.
 */
public class RentedCopyState implements CopyState {

    @Override
    public boolean canRent() {
        return false;
    }

    @Override
    public boolean canReserve() {
        return false;
    }

    @Override
    public boolean canReturn() {
        return true;
    }

    @Override
    public boolean canExtend() {
        return true;
    }

    @Override
    public void validateForRent() {
        throw new IllegalStateException("Cannot rent a copy that is already rented");
    }

    @Override
    public void validateForReservation() {
        throw new IllegalStateException("Cannot reserve a copy that is already rented");
    }

    @Override
    public void validateForReturn() {
        // Rented copy can be returned - no validation error
    }

    @Override
    public void validateForExtension() {
        // Rented copy can be extended - no validation error
    }

    @Override
    public String getStateName() {
        return "RENTED";
    }
}

