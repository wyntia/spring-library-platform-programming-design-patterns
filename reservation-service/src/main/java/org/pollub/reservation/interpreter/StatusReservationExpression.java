package org.pollub.reservation.interpreter;

import org.pollub.reservation.model.ReservationHistory;
import org.pollub.reservation.model.ReservationStatus;
import java.util.List;
import java.util.stream.Collectors;

//start L5 Interpreter
public class StatusReservationExpression implements ReservationSearchExpression {
    private final ReservationStatus status;

    public StatusReservationExpression(ReservationStatus status) {
        this.status = status;
    }

    @Override
    public List<ReservationHistory> interpret(List<ReservationHistory> reservations) {
        if (status == null) return reservations;
        return reservations.stream()
                .filter(r -> r.getStatus() == status)
                .collect(Collectors.toList());
    }
}
//end L5 Interpreter
