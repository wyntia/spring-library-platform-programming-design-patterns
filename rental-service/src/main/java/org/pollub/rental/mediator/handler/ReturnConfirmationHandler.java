package org.pollub.rental.mediator.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pollub.common.exception.ServiceException;
import org.pollub.common.mediator.Mediator;
import org.pollub.common.mediator.RequestHandler;
import org.pollub.rental.bridge.INotificationBridge;
import org.pollub.rental.mediator.request.GetItemTitleRequest;
import org.pollub.rental.mediator.request.GetUserEmailRequest;
import org.pollub.rental.mediator.request.SendReturnConfirmationNotification;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

//Lab5 Mediator Start
@Component
@RequiredArgsConstructor
@Slf4j
public class ReturnConfirmationHandler implements RequestHandler<SendReturnConfirmationNotification, Void> {

    @Lazy
    private final Mediator mediator;
    private final INotificationBridge notificationBridge;

    @Override
    public Void handle(SendReturnConfirmationNotification request) {
        //Lab6 : Wyjątki zamiast kodów błędów — przykład 3 Start
        String email;
        try {
            email = mediator.send(new GetUserEmailRequest(request.userId()));
        } catch (ServiceException e) {
            log.warn("Could not find email for user {}, skipping return confirmation", request.userId());
            return null;
        }
        //Lab6 : Wyjątki zamiast kodów błędów — przykład 3 Stop

        String itemTitle = mediator.send(new GetItemTitleRequest(request.itemId()));
        notificationBridge.sendReturnConfirmation(email, itemTitle);
        return null;
    }
}
//Lab5 Mediator End
