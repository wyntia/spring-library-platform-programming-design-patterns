package org.pollub.reservation.mediator.request;

import org.pollub.common.dto.ItemDto;
import org.pollub.common.mediator.Request;
import org.pollub.reservation.model.dto.ReservationCatalogRequestDto;

//Lab5 Mediator Start
public record MarkCatalogAsReservedRequest(
        ReservationCatalogRequestDto dto
) implements Request<ItemDto> {
}
//Lab5 Mediator End
