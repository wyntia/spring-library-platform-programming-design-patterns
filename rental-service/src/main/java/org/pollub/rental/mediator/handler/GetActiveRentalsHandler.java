package org.pollub.rental.mediator.handler;

import lombok.RequiredArgsConstructor;
import org.pollub.common.dto.ItemDto;
import org.pollub.common.mediator.RequestHandler;
import org.pollub.rental.client.CatalogServiceClient;
import org.pollub.rental.mediator.request.GetActiveRentalsRequest;
import org.springframework.stereotype.Component;

import java.util.List;

//Lab5 Mediator Start
@Component
@RequiredArgsConstructor
public class GetActiveRentalsHandler implements RequestHandler<GetActiveRentalsRequest, List<ItemDto>> {

    private final CatalogServiceClient catalogServiceClient;

    @Override
    public List<ItemDto> handle(GetActiveRentalsRequest request) {
        return catalogServiceClient.getItemsByUser(request.userId());
    }
}
//Lab5 Mediator End
