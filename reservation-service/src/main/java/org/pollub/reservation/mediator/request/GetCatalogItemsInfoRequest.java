package org.pollub.reservation.mediator.request;

import org.pollub.common.dto.ReservationItemDto;
import org.pollub.common.mediator.Request;

import java.util.List;

//L3 Mediator Start
public record GetCatalogItemsInfoRequest(
        List<Long> itemIds
) implements Request<List<ReservationItemDto.Item>> {
}
//L3 Mediator End
