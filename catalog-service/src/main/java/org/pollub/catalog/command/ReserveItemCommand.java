package org.pollub.catalog.command;

import org.pollub.catalog.model.dto.BranchInventoryDto;
import org.pollub.catalog.model.dto.ReservationCatalogRequestDto;
import org.pollub.catalog.service.IBranchInventoryService;

//start L3 Command
public class ReserveItemCommand implements Command<BranchInventoryDto> {
    private final IBranchInventoryService branchInventoryService;
    private final Long itemId;
    private final ReservationCatalogRequestDto reservationCatalogRequestDto;

    public ReserveItemCommand(IBranchInventoryService branchInventoryService, Long itemId, ReservationCatalogRequestDto reservationCatalogRequestDto) {
        this.branchInventoryService = branchInventoryService;
        this.itemId = itemId;
        this.reservationCatalogRequestDto = reservationCatalogRequestDto;
    }

    @Override
    public BranchInventoryDto execute() {
        return branchInventoryService.reserveCopy(itemId, reservationCatalogRequestDto);
    }
}
//end L3 Command