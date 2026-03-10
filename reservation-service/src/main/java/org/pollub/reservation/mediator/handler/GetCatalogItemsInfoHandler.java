package org.pollub.reservation.mediator.handler;

import lombok.RequiredArgsConstructor;
import org.pollub.common.dto.ReservationItemDto;
import org.pollub.common.mediator.RequestHandler;
import org.pollub.reservation.client.CatalogServiceClient;
import org.pollub.reservation.mediator.request.GetCatalogItemsInfoRequest;
import org.springframework.stereotype.Component;

import java.util.List;

//Lab5 Mediator Start
@Component
@RequiredArgsConstructor
public class GetCatalogItemsInfoHandler implements RequestHandler<GetCatalogItemsInfoRequest, List<ReservationItemDto.Item>> {

    private final CatalogServiceClient catalogServiceClient;

    @Override
    public List<ReservationItemDto.Item> handle(GetCatalogItemsInfoRequest request) {
        return catalogServiceClient.getItemsInfo(request.itemIds());
    }
}
//Lab5 Mediator End
