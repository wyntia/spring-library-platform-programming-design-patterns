package org.pollub.rental.mediator.request;

import org.pollub.common.mediator.Request;

//Lab5 Mediator Start
public record SendOverdueReminderNotification(
        Long rentalId
) implements Request<Void> {
}
//Lab5 Mediator End
