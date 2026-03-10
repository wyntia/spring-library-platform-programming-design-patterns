package org.pollub.reservation.mediator.handler;

import lombok.RequiredArgsConstructor;
import org.pollub.common.mediator.RequestHandler;
import org.pollub.reservation.client.CatalogServiceClient;
import org.pollub.reservation.mediator.request.UpdateCatalogStatusRequest;
import org.springframework.stereotype.Component;

//Lab5 Mediator Start
@Component
@RequiredArgsConstructor
public class UpdateCatalogStatusHandler implements RequestHandler<UpdateCatalogStatusRequest, Void> {

    private final CatalogServiceClient catalogServiceClient;

    @Override
    public Void handle(UpdateCatalogStatusRequest request) {
        catalogServiceClient.updateStatus(request.itemId(), request.branchId(), request.status());
        return null;
    }
}
//Lab5 Mediator End
