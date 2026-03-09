package org.pollub.reservation.service;

import org.pollub.reservation.mediator.AuditService;
import org.pollub.reservation.mediator.NotificationService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
class NotificationServiceImpl implements NotificationService {
    @Override
    public void notifyUser(Long userId, String message) {
        log.info("Powiadomienie dla użytkownika {}: {}", userId, message);
    }

    @Override
    public void notifyAdmin(String message) {
        log.info("Powiadomienie dla Admina: {}", message);
    }
}

@Service
@Slf4j
class AuditServiceImpl implements AuditService {
    @Override
    public void logReservation(Long userId, Long bookId) {
        log.info("AUDYT: Użytkownik {} zarezerwował książkę {}", userId, bookId);
    }


}