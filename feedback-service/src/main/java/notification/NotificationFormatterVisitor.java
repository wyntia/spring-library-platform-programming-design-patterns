package notification;

//L6 Visitor Design Pattern - Visitor interface for formatting notifications
/**
 * Concrete Visitor that formats messages specifically for each channel.
 */
public class NotificationFormatterVisitor implements NotificationVisitor {
    private String formattedMessage;
    private final String rawMessage;

    public NotificationFormatterVisitor(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    @Override
    public void visitEmail(EmailNotification email) {
        // Rich HTML formatting for emails
        this.formattedMessage = "<html><body>" +
                "<h3>Notification</h3>" +
                "<p>" + rawMessage + "</p>" +
                "<hr><small>Library System</small>" +
                "</body></html>";
    }

    @Override
    public void visitSms(SmsNotification sms) {
        // Plain text formatting optimized for SMS (no new lines, limited chars)
        this.formattedMessage = "LIB-MSG: " + rawMessage.replace("\n", " ");
    }

    public String getFormattedMessage() {
        return formattedMessage;
    }
}
