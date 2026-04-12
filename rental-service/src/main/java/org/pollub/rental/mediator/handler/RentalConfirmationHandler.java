package org.pollub.rental.mediator.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pollub.common.exception.ServiceException;
import org.pollub.common.mediator.Mediator;
import org.pollub.common.mediator.RequestHandler;
import org.pollub.rental.bridge.INotificationBridge;
import org.pollub.rental.mediator.request.GetItemTitleRequest;
import org.pollub.rental.mediator.request.GetUserEmailRequest;
import org.pollub.rental.mediator.request.SendRentalConfirmationNotification;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

//Lab5 Mediator Start
@Component
@RequiredArgsConstructor
@Slf4j
public class RentalConfirmationHandler implements RequestHandler<SendRentalConfirmationNotification, Void> {

    @Lazy
    private final Mediator mediator;
    private final INotificationBridge notificationBridge;

    @Override
    public Void handle(SendRentalConfirmationNotification request) {
        //Lab6 : Wyjątki zamiast kodów błędów — przykład 3 Start
        String email;
        try {
            email = mediator.send(new GetUserEmailRequest(request.userId()));
        } catch (ServiceException e) {
            log.warn("Could not find email for user {}, skipping rental confirmation", request.userId());
            return null;
        }
        //Lab6 : Wyjątki zamiast kodów błędów — przykład 3 Stop

        String itemTitle = mediator.send(new GetItemTitleRequest(request.itemId()));
        notificationBridge.sendRentalConfirmation(email, itemTitle, request.dueDate());
        return null;
    }
}
//Lab5 Mediator End
