package org.pollub.catalog.mediator.handler;

import lombok.RequiredArgsConstructor;
import org.pollub.common.mediator.RequestHandler;
import org.pollub.catalog.mediator.request.UpdateItemInventoryStatusRequest;
import org.pollub.catalog.service.IBranchInventoryService;
import org.springframework.stereotype.Component;

//Lab5 Mediator Start
@Component
@RequiredArgsConstructor
public class UpdateItemInventoryStatusHandler implements RequestHandler<UpdateItemInventoryStatusRequest, Void> {

    private final IBranchInventoryService branchInventoryService;

    @Override
    public Void handle(UpdateItemInventoryStatusRequest request) {
        branchInventoryService.updateStatus(request.itemId(), request.branchId(), request.status());
        return null;
    }
}
//Lab5 Mediator End
