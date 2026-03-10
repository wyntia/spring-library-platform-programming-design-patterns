package org.pollub.catalog.mediator.handler;

import lombok.RequiredArgsConstructor;
import org.pollub.common.mediator.RequestHandler;
import org.pollub.catalog.mediator.request.GetItemsForReservationRequest;
import org.pollub.catalog.service.ICatalogService;
import org.pollub.common.dto.ReservationItemDto;
import org.springframework.stereotype.Component;

import java.util.List;

//Lab5 Mediator Start
@Component
@RequiredArgsConstructor
public class GetItemsForReservationHandler implements RequestHandler<GetItemsForReservationRequest, List<ReservationItemDto.Item>> {

    private final ICatalogService catalogService;

    @Override
    public List<ReservationItemDto.Item> handle(GetItemsForReservationRequest request) {
        return catalogService.getItemsForReservation(request.itemIds());
    }
}
//Lab5 Mediator End
