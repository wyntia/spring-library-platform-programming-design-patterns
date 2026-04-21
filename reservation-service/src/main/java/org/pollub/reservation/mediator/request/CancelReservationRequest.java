package org.pollub.reservation.mediator.request;

import org.pollub.common.mediator.Request;

//L3 Mediator Start
public record CancelReservationRequest(
        Long id,
        Long userId
) implements Request<Void> {
}
//L3 Mediator End
