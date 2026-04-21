package org.pollub.rental.mediator.request;

import org.pollub.common.mediator.Request;

//L3 Mediator Start
public record GetItemTitleRequest(
        Long itemId
) implements Request<String> {
}
//L3 Mediator End
