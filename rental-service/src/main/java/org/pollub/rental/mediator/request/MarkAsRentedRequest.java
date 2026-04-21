package org.pollub.rental.mediator.request;

import org.pollub.common.dto.RentalHistoryDto;
import org.pollub.common.dto.ReservationResponse;
import org.pollub.common.mediator.Request;

//L3 Mediator Start
public record MarkAsRentedRequest(
        RentalHistoryDto rentalHistoryDto
) implements Request<ReservationResponse> {
}
//L3 Mediator End
