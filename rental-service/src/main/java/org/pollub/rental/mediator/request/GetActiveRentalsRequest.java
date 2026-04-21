package org.pollub.rental.mediator.request;

import org.pollub.common.dto.ItemDto;
import org.pollub.common.mediator.Request;

import java.util.List;

//L3 Mediator Start
public record GetActiveRentalsRequest(
        Long userId
) implements Request<List<ItemDto>> {
}
//L3 Mediator End
