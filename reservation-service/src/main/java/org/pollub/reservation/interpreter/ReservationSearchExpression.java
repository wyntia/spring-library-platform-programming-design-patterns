package org.pollub.reservation.interpreter;

import org.pollub.reservation.model.ReservationHistory;
import java.util.List;

//start L3 Interpreter
public interface ReservationSearchExpression {
    List<ReservationHistory> interpret(List<ReservationHistory> reservations);
}
//end L3 Interpreter
