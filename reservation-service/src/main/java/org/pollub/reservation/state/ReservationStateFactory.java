package org.pollub.reservation.state;

//L6 State Design Pattern - Factory for creating ReservationState instances based on ReservationStatus
import org.pollub.reservation.model.ReservationStatus;

public class ReservationStateFactory {
    public static ReservationState createState(ReservationStatus status) {
        if (status == null) {
            return new ActiveReservationState();
        }
        return switch (status) {
            case ACTIVE -> new ActiveReservationState();
            case FULFILLED -> new FulfilledReservationState();
            case CANCELLED -> new CancelledReservationState();
            case EXPIRED -> new ExpiredReservationState();
        };
    }
}
