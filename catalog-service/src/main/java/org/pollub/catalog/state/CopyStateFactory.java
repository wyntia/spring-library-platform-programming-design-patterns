package org.pollub.catalog.state;

import org.pollub.catalog.model.CopyStatus;

//L3 State Design Pattern - Factory for creating CopyState instances based on CopyStatus
/**
 * Factory for creating CopyState instances based on CopyStatus enum.
 * Ensures consistent state instantiation throughout the application.
 */
public class CopyStateFactory {

    /**
     * Create appropriate CopyState based on CopyStatus
     */
    public static CopyState createState(CopyStatus status) {
        if (status == null) {
            return new AvailableCopyState();
        }

        return switch (status) {
            case AVAILABLE -> new AvailableCopyState();
            case RENTED -> new RentedCopyState();
            case RESERVED -> new ReservedCopyState();
        };
    }
}

