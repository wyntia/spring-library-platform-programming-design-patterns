package org.pollub.reservation.mediator.request;

import org.pollub.common.mediator.Request;

//Lab5 Mediator Start
public record CancelReservationRequest(
        Long id,
        Long userId
) implements Request<Void> {
}
//Lab5 Mediator End
