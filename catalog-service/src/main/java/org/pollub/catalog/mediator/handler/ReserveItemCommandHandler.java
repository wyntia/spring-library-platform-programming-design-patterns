package org.pollub.catalog.mediator.handler;

import lombok.RequiredArgsConstructor;
import org.pollub.catalog.command.ReserveItemCommand;
import org.pollub.common.mediator.RequestHandler;
import org.pollub.catalog.mediator.request.MarkAsReservedRequest;
import org.pollub.catalog.model.dto.BranchInventoryDto;
import org.pollub.catalog.service.IBranchInventoryService;
import org.springframework.stereotype.Component;

//Lab5 Mediator Start
@Component
@RequiredArgsConstructor
public class ReserveItemCommandHandler implements RequestHandler<MarkAsReservedRequest, BranchInventoryDto> {

    private final IBranchInventoryService branchInventoryService;

    @Override
    public BranchInventoryDto handle(MarkAsReservedRequest request) {
        //Lab5 Command 1 Start
        ReserveItemCommand command = new ReserveItemCommand(
                branchInventoryService, request.itemId(), request.dto()
        );
        return command.execute();
        //Lab5 Command 1 End
    }
}
//Lab5 Mediator End
