package org.pollub.catalog.mediator.request;

import org.pollub.common.mediator.Request;

//L3 Mediator Start
public record UpdateItemInventoryStatusRequest(
        Long itemId,
        Long branchId,
        String status
) implements Request<Void> {
}
//L3 Mediator End
