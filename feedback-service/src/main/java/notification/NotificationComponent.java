//start L3 Composite
package notification;

/**
 * Base interface for the notification composite pattern.
 */
public interface NotificationComponent {
    void send(String message);

    // start L3 Visitor
    void accept(NotificationVisitor visitor);
    // end L3 Visitor
}
//end L3 Composite
