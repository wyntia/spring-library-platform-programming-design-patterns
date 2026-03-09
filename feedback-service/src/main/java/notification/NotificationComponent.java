//start L3 Composite
package notification;

/**
 * Base interface for the notification composite pattern.
 */
public interface NotificationComponent {
    void send(String message);

    // start L6 Visitor
    void accept(NotificationVisitor visitor);
    // end L6 Visitor
}
//end L3 Composite
