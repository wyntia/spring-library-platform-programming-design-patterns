package org.pollub.rental.mediator.request;

import org.pollub.common.mediator.Request;

//Lab5 Mediator Start
public record SendReturnConfirmationNotification(
        Long userId,
        Long itemId
) implements Request<Void> {
}
//Lab5 Mediator End
