package org.pollub.common;

//start L3 Observer
/**
 * Subject interface for the Observer design pattern.
 * Subjects maintain a list of observers and notify them of changes.
 */
public interface Subject {

    /**
     * Attach an observer to this subject.
     *
     * @param observer the observer to attach
     */
    void attach(Observer observer);

    /**
     * Detach an observer from this subject.
     *
     * @param observer the observer to detach
     */
    void detach(Observer observer);

    /**
     * Notify all attached observers of a change.
     *
     * @param event the event or data to notify
     */
    void notifyObservers(Object event);
}
//stop L3 Observer