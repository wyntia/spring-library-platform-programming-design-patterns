package org.pollub.reservation.mediator.request;

import org.pollub.common.dto.ItemDto;
import org.pollub.common.mediator.Request;
import org.pollub.reservation.model.dto.ReservationDto;

//L3 Mediator Start
public record CreateReservationRequest(
        ReservationDto dto,
        Long userId
) implements Request<ItemDto> {
}
//L3 Mediator End
