package org.pollub.rental.mediator.request;

import org.pollub.common.mediator.Request;

//L3 Mediator Start
public record MarkAsReturnedRequest(
        Long itemId,
        Long branchId
) implements Request<Void> {
}
//L3 Mediator End
