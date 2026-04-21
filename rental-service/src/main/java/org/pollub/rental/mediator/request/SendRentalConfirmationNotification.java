package org.pollub.rental.mediator.request;

import org.pollub.common.mediator.Request;

import java.time.LocalDateTime;

//L3 Mediator Start
public record SendRentalConfirmationNotification(
        Long userId,
        Long itemId,
        LocalDateTime dueDate
) implements Request<Void> {
}
//L3 Mediator End
