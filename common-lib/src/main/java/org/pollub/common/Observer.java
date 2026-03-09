package org.pollub.common;

//start L6 Observer interface
/**
 * Observer interface for the Observer design pattern.
 * Observers are notified of changes in the subject.
 */
public interface Observer {

    /**
     * Called when the subject notifies observers of a change.
     *
     * @param subject the subject that changed
     * @param event   the event or data related to the change
     */
    void update(Subject subject, Object event);
}
//stop L6 Observer interface