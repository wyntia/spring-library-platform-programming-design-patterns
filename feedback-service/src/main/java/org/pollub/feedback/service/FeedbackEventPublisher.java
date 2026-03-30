package org.pollub.feedback.service;

import lombok.extern.slf4j.Slf4j;
import org.pollub.common.Observer;
import org.pollub.common.Subject;
import org.pollub.common.config.DateTimeProvider;
import org.pollub.common.event.FeedbackEvent;
import org.pollub.feedback.model.Feedback;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
//Lab4 - SRP 3 Start
public class FeedbackEventPublisher implements Subject {

    private final List<Observer> observers = new ArrayList<>();

    public void publishFeedbackSubmitted(Feedback feedback) {
        notifyObservers(new FeedbackEvent(
                "FEEDBACK_SUBMITTED",
                feedback.getId(),
                feedback.getCategory().name(),
                feedback.getMessage(),
                feedback.getIpAddress(),
                null,
                feedback.getStatus().name(),
                DateTimeProvider.getInstance().now()
        ));
    }

    public void publishStatusChanged(Feedback feedback, String oldStatus, String newStatus) {
        notifyObservers(new FeedbackEvent(
                "STATUS_CHANGED_" + newStatus,
                feedback.getId(),
                feedback.getCategory().name(),
                feedback.getMessage(),
                feedback.getIpAddress(),
                oldStatus,
                newStatus,
                DateTimeProvider.getInstance().now()
        ));
    }

    @Override
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            log.debug("Observer attached: {}", observer.getClass().getSimpleName());
        }
    }

    @Override
    public void detach(Observer observer) {
        if (observers.remove(observer)) {
            log.debug("Observer detached: {}", observer.getClass().getSimpleName());
        }
    }

    @Override
    public void notifyObservers(Object event) {
        for (Observer observer : observers) {
            observer.update(this, event);
        }
    }
}
//SRP3 End