package org.pollub.rental.state;

import org.pollub.rental.model.RentalStatus;

//L3 State Design Pattern - Factory for creating RentalState instances based on RentalStatus
/**
 * Factory for creating RentalState instances based on RentalStatus enum.
 */
public class RentalStateFactory {

    public static RentalState createState(RentalStatus status) {
        if (status == null) {
            return new RentedState();
        }

        return switch (status) {
            case RENTED -> new RentedState();
            case RETURNED -> new ReturnedState();
            case OVERDUE -> new OverdueState();
        };
    }
}

