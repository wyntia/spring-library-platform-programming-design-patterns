package org.pollub.catalog.mediator.request;

import org.pollub.common.mediator.Request;
import org.pollub.common.dto.ReservationItemDto;

import java.util.List;

//L3 Mediator Start
public record GetItemsForReservationRequest(
        List<Long> itemIds
) implements Request<List<ReservationItemDto.Item>> {
}
//L3 Mediator End
