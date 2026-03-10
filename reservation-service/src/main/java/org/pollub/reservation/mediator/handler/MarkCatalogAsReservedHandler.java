package org.pollub.reservation.mediator.handler;

import lombok.RequiredArgsConstructor;
import org.pollub.common.dto.ItemDto;
import org.pollub.common.mediator.RequestHandler;
import org.pollub.reservation.client.CatalogServiceClient;
import org.pollub.reservation.mediator.request.MarkCatalogAsReservedRequest;
import org.springframework.stereotype.Component;

//Lab5 Mediator Start
@Component
@RequiredArgsConstructor
public class MarkCatalogAsReservedHandler implements RequestHandler<MarkCatalogAsReservedRequest, ItemDto> {

    private final CatalogServiceClient catalogServiceClient;

    @Override
    public ItemDto handle(MarkCatalogAsReservedRequest request) {
        return catalogServiceClient.markAsReserved(request.dto());
    }
}
//Lab5 Mediator End
