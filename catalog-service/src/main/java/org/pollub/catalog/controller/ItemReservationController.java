package org.pollub.catalog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pollub.common.mediator.Mediator;
import org.pollub.catalog.mediator.request.GetItemsForReservationRequest;
import org.pollub.catalog.mediator.request.MarkAsReservedRequest;
import org.pollub.catalog.mediator.request.UpdateItemInventoryStatusRequest;
import org.pollub.catalog.model.dto.BranchInventoryDto;
import org.pollub.catalog.model.dto.ReservationCatalogRequestDto;
import org.pollub.catalog.model.dto.UpdateInventoryStatusRequest;
import org.pollub.common.dto.ReservationItemDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemReservationController {

    //Lab5 Mediator Start
    private final Mediator mediator;

    @PutMapping("/{itemId}/reserve")
    public ResponseEntity<BranchInventoryDto> markAsReserved(
            @PathVariable Long itemId,
            @RequestBody ReservationCatalogRequestDto reservationCatalogRequestDto
    ) {
        BranchInventoryDto result = mediator.send(
                new MarkAsReservedRequest(itemId, reservationCatalogRequestDto)
        );
        return ResponseEntity.ok(result);
    }

    // POST for getting because of possible large list of IDs
    @PostMapping("/info/batch")
    public ResponseEntity<List<ReservationItemDto.Item>> getItemsForReservation(
            @RequestBody List<Long> itemIds
    ) {
        List<ReservationItemDto.Item> items = mediator.send(
                new GetItemsForReservationRequest(itemIds)
        );
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{itemId}/inventory/{branchId}/status")
    public ResponseEntity<Void> updateInventoryStatus(
            @PathVariable Long itemId,
            @PathVariable Long branchId,
            @Valid @RequestBody UpdateInventoryStatusRequest request
    ) {
        mediator.send(
                new UpdateItemInventoryStatusRequest(itemId, branchId, request.getStatus())
        );
        return ResponseEntity.noContent().build();
    }
    //Lab5 Mediator End
}