//start L3 Decorator
package notification;

/**
* Logging decorator implementation for the Notification pattern.
 */
public class LoggingNotificationDecorator implements NotificationComponent {
    private final NotificationComponent delegate;
    public LoggingNotificationDecorator(NotificationComponent delegate) {
        this.delegate = delegate;
    }
    @Override
    public void send(String message) {
        System.out.println("[LOG] Rozpoczynam wysyłkę powiadomienia: " + message);
        delegate.send(message);
        System.out.println("[LOG] Powiadomienie wysłane: " + message);
    }
}
//end L3 Decorator
