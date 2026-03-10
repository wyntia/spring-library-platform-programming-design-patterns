package org.pollub.rental.mediator.request;

import org.pollub.common.mediator.Request;

//Lab5 Mediator Start
public record GetUserEmailRequest(
        Long userId
) implements Request<String> {
}
//Lab5 Mediator End
