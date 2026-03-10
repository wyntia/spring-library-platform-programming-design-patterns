package org.pollub.rental.mediator.handler;

import lombok.RequiredArgsConstructor;
import org.pollub.common.mediator.RequestHandler;
import org.pollub.rental.client.CatalogServiceClient;
import org.pollub.rental.mediator.request.ExtendRentalRequest;
import org.springframework.stereotype.Component;

//Lab5 Mediator Start
@Component
@RequiredArgsConstructor
public class ExtendRentalHandler implements RequestHandler<ExtendRentalRequest, Void> {

    private final CatalogServiceClient catalogServiceClient;

    @Override
    public Void handle(ExtendRentalRequest request) {
        catalogServiceClient.extendRental(request.itemId(), request.branchId(), request.days());
        return null;
    }
}
//Lab5 Mediator End
