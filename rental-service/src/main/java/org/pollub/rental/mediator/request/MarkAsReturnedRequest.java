package org.pollub.rental.mediator.request;

import org.pollub.common.mediator.Request;

//Lab5 Mediator Start
public record MarkAsReturnedRequest(
        Long itemId,
        Long branchId
) implements Request<Void> {
}
//Lab5 Mediator End
