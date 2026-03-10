package org.pollub.reservation.mediator.request;

import org.pollub.common.dto.ReservationItemDto;
import org.pollub.common.mediator.Request;

import java.util.List;

//Lab5 Mediator Start
public record GetUserReservationsRequest(
        Long userId
) implements Request<List<ReservationItemDto>> {
}
//Lab5 Mediator End
