package org.pollub.reservation.mediator;

public interface NotificationService {
    void notifyUser(Long userId, String message);
    void notifyAdmin(String message);
}
