package org.pollub.catalog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pollub.catalog.model.dto.BranchInventoryDto;
import org.pollub.catalog.model.dto.ReservationCatalogRequestDto;
import org.pollub.catalog.model.dto.UpdateInventoryStatusRequest;
import org.pollub.catalog.service.IBranchInventoryService;
import org.pollub.catalog.service.ICatalogService;
import org.pollub.catalog.command.ReserveItemCommand;
import org.pollub.catalog.command.Command;
import org.pollub.common.dto.ReservationItemDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemReservationController {

    private final ICatalogService catalogService;
    private final IBranchInventoryService branchInventoryService;

    @PutMapping("/{itemId}/reserve")
        public ResponseEntity<BranchInventoryDto> markAsReserved(
            @PathVariable Long itemId,
            @RequestBody ReservationCatalogRequestDto reservationCatalogRequestDto
        ) {
        //start L5 Command
        Command<BranchInventoryDto> command = new ReserveItemCommand(branchInventoryService, itemId, reservationCatalogRequestDto);
        BranchInventoryDto branchInventory = command.execute();
        ResponseEntity<BranchInventoryDto> response = ResponseEntity.ok(branchInventory);
        //end L5 Command
        return response;
        }

    // POST for getting because of possible large list of IDs
    @PostMapping("/info/batch")
    public ResponseEntity<List<ReservationItemDto.Item>> getItemsForReservation(
            @RequestBody List<Long> itemIds
    ) {
        List<ReservationItemDto.Item> items = catalogService.getItemsForReservation(itemIds);
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{itemId}/inventory/{branchId}/status")
    public ResponseEntity<Void> updateInventoryStatus(
            @PathVariable Long itemId,
            @PathVariable Long branchId,
            @Valid @RequestBody UpdateInventoryStatusRequest request
    ) {
        String statusStr = request.getStatus();

        branchInventoryService.updateStatus(itemId, branchId, statusStr);
        return ResponseEntity.noContent().build();
    }




}