package org.pollub.rental.mediator.support;

import org.pollub.common.exception.ServiceException;
import org.pollub.common.mediator.Mediator;
import org.pollub.rental.mediator.request.GetUserEmailRequest;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;

//Lab6 : Brak powtórzeń (DRY) Start
/**
 * Resolves user email for rental notifications via the mediator, mapping lookup failures to empty result.
 */
@Component
public class NotificationUserEmailResolver {

    private final Mediator mediator;

    public NotificationUserEmailResolver(@Lazy Mediator mediator) {
        this.mediator = mediator;
    }

    /**
     * @param onServiceFailure invoked when user-service lookup fails (e.g. log and skip notification)
     */
    public Optional<String> resolveEmail(Long userId, Runnable onServiceFailure) {
        try {
            return Optional.of(mediator.send(new GetUserEmailRequest(userId)));
        } catch (ServiceException e) {
            onServiceFailure.run();
            return Optional.empty();
        }
    }
}
//Lab6 : Brak powtórzeń (DRY) Stop
