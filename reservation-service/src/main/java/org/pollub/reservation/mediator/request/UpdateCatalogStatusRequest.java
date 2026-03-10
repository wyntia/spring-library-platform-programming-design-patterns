package org.pollub.reservation.mediator.request;

import org.pollub.common.mediator.Request;

//Lab5 Mediator Start
public record UpdateCatalogStatusRequest(
        Long itemId,
        Long branchId,
        String status
) implements Request<Void> {
}
//Lab5 Mediator End
