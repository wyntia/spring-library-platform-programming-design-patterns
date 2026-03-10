package org.pollub.reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pollub.common.dto.ItemDto;
import org.pollub.common.dto.ReservationItemDto;
import org.pollub.common.mediator.Mediator;
import org.pollub.common.security.JwtUserDetails;
import org.pollub.reservation.mediator.request.CancelReservationRequest;
import org.pollub.reservation.mediator.request.CreateReservationRequest;
import org.pollub.reservation.mediator.request.FulfillReservationRequest;
import org.pollub.reservation.mediator.request.GetUserReservationsRequest;
import org.pollub.reservation.model.dto.ReservationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    //Lab5 Mediator Start
    private final Mediator mediator;
    //Lab5 Mediator End

    @GetMapping("/my")
    public ResponseEntity<List<ReservationItemDto>> getMyReservations(
            @AuthenticationPrincipal JwtUserDetails user
    ) {
        //Lab5 Mediator Start
        List<ReservationItemDto> reservations = mediator.send(new GetUserReservationsRequest(user.getUserId()));
        //Lab5 Mediator End
        return ResponseEntity.ok(reservations);
    }

    @PostMapping()
    public ResponseEntity<ItemDto> createReservation(
            @Valid @RequestBody ReservationDto dto,
            @AuthenticationPrincipal JwtUserDetails user
    ) {
        //Lab5 Mediator Start
        ItemDto reservation = mediator.send(new CreateReservationRequest(dto, user.getUserId()));
        //Lab5 Mediator End
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUserDetails user
    ) {
        //Lab5 Mediator Start
        mediator.send(new CancelReservationRequest(id, user.getUserId()));
        //Lab5 Mediator End
        return ResponseEntity.noContent().build();
    }

    /**
     * Get active reservations for a specific user (for librarian use).
     * Used in loan-management to display user's reservations and allow loaning reserved books.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationItemDto>> getUserReservations(
            @PathVariable Long userId
    ) {
        //Lab5 Mediator Start
        List<ReservationItemDto> reservations = mediator.send(new GetUserReservationsRequest(userId));
        //Lab5 Mediator End
        return ResponseEntity.ok(reservations);
    }

    /**
     * Mark a reservation as fulfilled when the reserved book is rented.
     * Called by catalog-service when a reserved book is being rented.
     */
    @PutMapping("/fulfill")
    public ResponseEntity<Void> fulfillReservation(
            @RequestParam Long itemId,
            @RequestParam Long branchId,
            @RequestParam Long userId
    ) {
        //Lab5 Mediator Start
        mediator.send(new FulfillReservationRequest(itemId, branchId, userId));
        //Lab5 Mediator End
        return ResponseEntity.ok().build();
    }
}



