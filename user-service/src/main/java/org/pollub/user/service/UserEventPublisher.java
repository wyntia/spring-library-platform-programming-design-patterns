package org.pollub.user.service;

import lombok.extern.slf4j.Slf4j;
import org.pollub.common.Observer;
import org.pollub.common.Subject;
import org.pollub.common.config.DateTimeProvider;
import org.pollub.common.event.UserEvent;
import org.pollub.user.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class UserEventPublisher implements Subject {

    private final List<Observer> observers = new ArrayList<>();

    public void publish(String eventType, User user, String message) {
        if (user == null) {
            return;
        }

        notifyObservers(new UserEvent(
                eventType,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                message,
                DateTimeProvider.getInstance().now()
        ));
    }

    public void publish(String eventType, Long userId, String username, String email, String message) {
        notifyObservers(new UserEvent(
                eventType,
                userId,
                username,
                email,
                message,
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
