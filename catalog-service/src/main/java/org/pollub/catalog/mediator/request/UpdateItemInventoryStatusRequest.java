package org.pollub.catalog.mediator.request;

import org.pollub.common.mediator.Request;

//Lab5 Mediator Start
public record UpdateItemInventoryStatusRequest(
        Long itemId,
        Long branchId,
        String status
) implements Request<Void> {
}
//Lab5 Mediator End
