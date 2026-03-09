package org.pollub.branch.service;

import org.springframework.stereotype.Component;

@Component
public class NotificationService {
    public void notifyUser(Long userId, String message) {
        System.out.println("[NOTIFY USER] " + userId + ": " + message);
    }
    public void notifyAdmin(String message) {
        System.out.println("[NOTIFY ADMIN] " + message);
    }
}
