//start L3 Composite
package notification;

/**
 * Leaf implementation of the Composite pattern for SMS notifications.
 */
public class SmsNotification implements NotificationComponent {
    private final String phoneNumber;
    public SmsNotification(String phoneNumber) { this.phoneNumber = phoneNumber; }
    @Override
    public void send(String message) {
        System.out.println("Wysyłam SMS do: " + phoneNumber + " | Treść: " + message);
    }

    @Override
    public void accept(NotificationVisitor visitor) {
        visitor.visitSms(this); // L6 Visitor double dispatch
    }
}
//end L3 Composite
