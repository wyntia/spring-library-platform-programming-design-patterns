package org.pollub.rental.mediator.request;

import org.pollub.common.mediator.Request;

//Lab5 Mediator Start
public record GetItemTitleRequest(
        Long itemId
) implements Request<String> {
}
//Lab5 Mediator End
