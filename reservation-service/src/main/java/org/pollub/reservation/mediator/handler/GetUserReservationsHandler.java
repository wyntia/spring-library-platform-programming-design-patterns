package org.pollub.reservation.mediator.handler;

import lombok.RequiredArgsConstructor;
import org.pollub.common.dto.ReservationItemDto;
import org.pollub.common.mediator.Mediator;
import org.pollub.common.mediator.RequestHandler;
import org.pollub.reservation.interpreter.ReservationSearchExpression;
import org.pollub.reservation.interpreter.StatusReservationExpression;
import org.pollub.reservation.mediator.request.GetCatalogItemsInfoRequest;
import org.pollub.reservation.mediator.request.GetUserReservationsRequest;
import org.pollub.reservation.model.ReservationHistory;
import org.pollub.reservation.model.ReservationStatus;
import org.pollub.reservation.repository.ReservationRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

//Lab5 Mediator Start
@Component
@RequiredArgsConstructor
public class GetUserReservationsHandler implements RequestHandler<GetUserReservationsRequest, List<ReservationItemDto>> {

    private final ReservationRepository reservationRepository;
    @Lazy
    private final Mediator mediator;

    @Override
    public List<ReservationItemDto> handle(GetUserReservationsRequest request) {
        List<ReservationHistory> allReservations = reservationRepository.findByUserId(request.userId());

        //start L5 Interpreter
        ReservationSearchExpression expr = new StatusReservationExpression(ReservationStatus.ACTIVE);
        List<ReservationHistory> reservations = expr.interpret(allReservations);
        //end L5 Interpreter

        List<Long> itemIds = reservations.stream()
            .map(ReservationHistory::getItemId)
            .distinct()
            .toList();

        List<ReservationItemDto.Item> items = mediator.send(new GetCatalogItemsInfoRequest(itemIds));

        return reservations.stream()
            .map(reservation -> {
                ReservationItemDto.Item item = items.stream()
                    .filter(i -> i.getId().equals(reservation.getItemId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(
                        "Item info not found for itemId: " + reservation.getItemId()));

                return ReservationItemDto.builder()
                    .id(reservation.getId())
                    .branchId(reservation.getBranchId())
                    .expiresAt(reservation.getExpiresAt())
                    .item(ReservationItemDto.Item.builder()
                        .id(item.getId())
                        .title(item.getTitle())
                        .imageUrl(item.getImageUrl())
                        .build())
                    .build();
            })
            .toList();
    }
}
//Lab5 Mediator End
