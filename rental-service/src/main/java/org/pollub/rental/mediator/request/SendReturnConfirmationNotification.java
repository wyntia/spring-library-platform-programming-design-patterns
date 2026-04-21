package org.pollub.rental.mediator.request;

import org.pollub.common.mediator.Request;

//L3 Mediator Start
public record SendReturnConfirmationNotification(
        Long userId,
        Long itemId
) implements Request<Void> {
}
//L3 Mediator End
