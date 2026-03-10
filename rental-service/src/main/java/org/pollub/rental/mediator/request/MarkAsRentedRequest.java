package org.pollub.rental.mediator.request;

import org.pollub.common.dto.RentalHistoryDto;
import org.pollub.common.dto.ReservationResponse;
import org.pollub.common.mediator.Request;

//Lab5 Mediator Start
public record MarkAsRentedRequest(
        RentalHistoryDto rentalHistoryDto
) implements Request<ReservationResponse> {
}
//Lab5 Mediator End
