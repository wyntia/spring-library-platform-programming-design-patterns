//start L2 Composite
package notification;

/**
 * Composite Leaf - email notification
 */
public class EmailNotification implements NotificationComponent {
    private final String email;
    public EmailNotification(String email) { this.email = email; }
    @Override
    public void send(String message) {
        System.out.println("Wysyłam e-mail do: " + email + " | Treść: " + message);
    }

    @Override
    public void accept(NotificationVisitor visitor) {
        visitor.visitEmail(this); // L6 Visitor double dispatch
    }
}
//end L2 Composite
