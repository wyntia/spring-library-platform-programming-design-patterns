package org.pollub.catalog.mediator.request;

import org.pollub.common.mediator.Request;
import org.pollub.catalog.model.dto.BranchInventoryDto;
import org.pollub.catalog.model.dto.ReservationCatalogRequestDto;

//Lab5 Mediator Start
public record MarkAsReservedRequest(
        Long itemId,
        ReservationCatalogRequestDto dto
) implements Request<BranchInventoryDto> {
}
//Lab5 Mediator End
