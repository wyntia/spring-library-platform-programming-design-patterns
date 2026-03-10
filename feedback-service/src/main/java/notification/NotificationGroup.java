//start L2 Composite
package notification;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite implementation for a group of notification channels.
 * Forwards a single message to all registered notification components.
 */
public class NotificationGroup implements NotificationComponent {
    private final List<NotificationComponent> children = new ArrayList<>();
    public void add(NotificationComponent child) { children.add(child); }
    public void remove(NotificationComponent child) { children.remove(child); }
    @Override
    public void send(String message) {
        for (NotificationComponent child : children) {
            child.send(message);
        }
    }

    @Override
    public void accept(NotificationVisitor visitor) {
        // L6 Visitor propagation through composite structure
        for (NotificationComponent child : children) {
            child.accept(visitor);
        }
    }
}
//end L2 Composite
