package org.pollub.reservation.iterator;

import org.pollub.reservation.model.ReservationHistory;
import java.util.Iterator;
import java.util.List;

//start L5 Iterator
public class ReservationHistoryIterator implements Iterator<ReservationHistory> {
    private final List<ReservationHistory> reservations;
    private int position = 0;

    public ReservationHistoryIterator(List<ReservationHistory> reservations) {
        this.reservations = reservations;
    }

    @Override
    public boolean hasNext() {
        return position < reservations.size();
    }

    @Override
    public ReservationHistory next() {
        return reservations.get(position++);
    }
}
//end L5 Iterator
