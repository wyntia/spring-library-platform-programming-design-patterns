package notification;

//L6 Visitor Design Pattern - Visitor interface for notification operations
/**
 * Visitor interface for notification operations.
 * Decouples message formatting logic from notification channels.
 */
public interface NotificationVisitor {
    void visitEmail(EmailNotification email);
    void visitSms(SmsNotification sms);
}
