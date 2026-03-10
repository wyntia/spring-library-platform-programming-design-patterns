//start L2 Decorator
package notification;
/**
 * A template decorator for NotificationComponent.
 */
public class NotificationTemplateDecorator implements NotificationComponent {
    private final NotificationComponent delegate;
    private final String header;
    private final String footer;

    public NotificationTemplateDecorator(NotificationComponent delegate, String header, String footer) {
        this.delegate = delegate;
        this.header = header;
        this.footer = footer;
    }

    @Override
    public void send(String message) {
        String formatted = header + "\n" + message + "\n" + footer;
        delegate.send(formatted);
    }

    // start L6 Visitor
    @Override
    public void accept(NotificationVisitor visitor) {
        // Decorator forwards the visitor to the delegate
        delegate.accept(visitor);
    }
    // end L6 Visitor
}
//end L2 Decorator
