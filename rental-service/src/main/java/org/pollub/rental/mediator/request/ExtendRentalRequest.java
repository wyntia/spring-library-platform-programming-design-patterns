package org.pollub.rental.mediator.request;

import org.pollub.common.mediator.Request;

//Lab5 Mediator Start
public record ExtendRentalRequest(
        Long itemId,
        Long branchId,
        int days
) implements Request<Void> {
}
//Lab5 Mediator End
