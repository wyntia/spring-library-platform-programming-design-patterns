package org.pollub.catalog.mediator.request;

import org.pollub.common.mediator.Request;
import org.pollub.catalog.model.dto.BranchInventoryDto;
import org.pollub.catalog.model.dto.ReservationCatalogRequestDto;

//L3 Mediator Start
public record MarkAsReservedRequest(
        Long itemId,
        ReservationCatalogRequestDto dto
) implements Request<BranchInventoryDto> {
}
//L3 Mediator End
