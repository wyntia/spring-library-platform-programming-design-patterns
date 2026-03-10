package org.pollub.rental.mediator.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pollub.common.mediator.Mediator;
import org.pollub.common.mediator.RequestHandler;
import org.pollub.rental.bridge.INotificationBridge;
import org.pollub.rental.mediator.request.GetItemTitleRequest;
import org.pollub.rental.mediator.request.GetUserEmailRequest;
import org.pollub.rental.mediator.request.SendOverdueReminderNotification;
import org.pollub.rental.model.RentalHistory;
import org.springframework.context.annotation.Lazy;
import org.pollub.rental.repository.IRentalHistoryRepository;
import org.springframework.stereotype.Component;

//Lab5 Mediator Start
@Component
@RequiredArgsConstructor
@Slf4j
public class OverdueReminderHandler implements RequestHandler<SendOverdueReminderNotification, Void> {

    private final IRentalHistoryRepository rentalHistoryRepository;
    @Lazy
    private final Mediator mediator;
    private final INotificationBridge notificationBridge;

    @Override
    public Void handle(SendOverdueReminderNotification request) {
        RentalHistory rental = rentalHistoryRepository.findById(request.rentalId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Rental not found: " + request.rentalId()
                ));

        String email = mediator.send(new GetUserEmailRequest(rental.getUserId()));
        if (email == null) {
            log.warn("Could not find email for user {}, skipping reminder for rental {}",
                    rental.getUserId(), rental.getId());
            return null;
        }

        String itemTitle = mediator.send(new GetItemTitleRequest(rental.getItemId()));
        notificationBridge.sendRentalReminder(email, itemTitle, rental.getDueDate());
        return null;
    }
}
//Lab5 Mediator End
