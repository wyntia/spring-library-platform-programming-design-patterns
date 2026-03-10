package org.pollub.rental.bridge;

import java.time.LocalDateTime;

//start L2 Bridge Interface
/**
 * Bridge for abstracting notification methods.
 * Allows sending reminders via different channels (email, SMS, push, etc.)
 */
public interface INotificationBridge {
    
    /**
     * Send rental reminder notification.
     */
    void sendRentalReminder(String recipient, String itemTitle, LocalDateTime dueDate);

    //Lab5 Mediator Start
    /**
     * Send rental confirmation notification.
     */
    void sendRentalConfirmation(String recipient, String itemTitle, LocalDateTime dueDate);

    /**
     * Send return confirmation notification.
     */
    void sendReturnConfirmation(String recipient, String itemTitle);
    //Lab5 Mediator End

    /**
     * Get notification type.
     */
    String getNotificationType();
}
//end L2 Bridge Interface