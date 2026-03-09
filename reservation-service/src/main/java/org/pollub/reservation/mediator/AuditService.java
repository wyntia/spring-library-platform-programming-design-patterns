package org.pollub.reservation.mediator;

public interface AuditService {
    void logReservation(Long userId, Long bookId);
}
