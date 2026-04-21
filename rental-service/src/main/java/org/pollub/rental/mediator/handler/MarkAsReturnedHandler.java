package org.pollub.rental.mediator.handler;

import lombok.RequiredArgsConstructor;
import org.pollub.common.mediator.RequestHandler;
import org.pollub.rental.client.CatalogServiceClient;
import org.pollub.rental.mediator.request.MarkAsReturnedRequest;
import org.springframework.stereotype.Component;

//L3 Mediator Start
@Component
@RequiredArgsConstructor
public class MarkAsReturnedHandler implements RequestHandler<MarkAsReturnedRequest, Void> {

    private final CatalogServiceClient catalogServiceClient;

    @Override
    public Void handle(MarkAsReturnedRequest request) {
        catalogServiceClient.markAsReturned(request.itemId(), request.branchId());
        return null;
    }
}
//L3 Mediator End
