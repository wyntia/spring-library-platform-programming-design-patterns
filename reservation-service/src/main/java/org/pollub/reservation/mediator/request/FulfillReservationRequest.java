package org.pollub.reservation.mediator.request;

import org.pollub.common.mediator.Request;

//L3 Mediator Start
public record FulfillReservationRequest(
        Long itemId,
        Long branchId,
        Long userId
) implements Request<Void> {
}
//L3 Mediator End
