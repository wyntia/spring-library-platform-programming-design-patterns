package org.pollub.rental.mediator.handler;

import lombok.RequiredArgsConstructor;
import org.pollub.common.dto.ReservationResponse;
import org.pollub.common.mediator.RequestHandler;
import org.pollub.rental.client.CatalogServiceClient;
import org.pollub.rental.mediator.request.MarkAsRentedRequest;
import org.springframework.stereotype.Component;

//L3 Mediator Start
@Component
@RequiredArgsConstructor
public class MarkAsRentedHandler implements RequestHandler<MarkAsRentedRequest, ReservationResponse> {

    private final CatalogServiceClient catalogServiceClient;

    @Override
    public ReservationResponse handle(MarkAsRentedRequest request) {
        return catalogServiceClient.markAsRented(request.rentalHistoryDto());
    }
}
//L3 Mediator End
