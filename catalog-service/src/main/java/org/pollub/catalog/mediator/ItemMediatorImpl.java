package org.pollub.catalog.mediator;

import lombok.RequiredArgsConstructor;
import org.pollub.catalog.model.LibraryItem;
import org.pollub.catalog.service.ICatalogService;
import org.springframework.stereotype.Component;
import java.util.List;
import org.pollub.catalog.model.dto.HistoryCatalogResponse;
import java.util.Map;

//start L5 Mediator
@Component
@RequiredArgsConstructor

public class ItemMediatorImpl implements ItemMediator {
    private final ICatalogService catalogService;

    @Override
    public List<LibraryItem> findAll() {
        return catalogService.findAll();
    }

    @Override
    public LibraryItem findById(Long id) {
        return catalogService.findById(id);
    }

    @Override
    public List<LibraryItem> findAvailable() {
        return catalogService.findAvailable();
    }

    @Override
    public List<LibraryItem> findRented() {
        return catalogService.findRented();
    }

    @Override
    public List<LibraryItem> findByBranchId(Long branchId) {
        return catalogService.findByBranchId(branchId);
    }

    @Override
    public List<LibraryItem> findAvailableByBranch(Long branchId) {
        return catalogService.findAvailableByBranch(branchId);
    }

    @Override
    public List<LibraryItem> searchItems(String query) {
        return catalogService.searchItems(query);
    }

    @Override
    public List<LibraryItem> findBestsellers() {
        return catalogService.findBestsellers();
    }

    @Override
    public void deleteItem(Long id) {
        catalogService.deleteItem(id);
    }

    @Override
    public Map<Long, HistoryCatalogResponse> getHistoryCatalogDataByItemIds(List<Long> itemIds) {
        return catalogService.getHistoryCatalogDataByItemIds(itemIds);
    }
}
//end L5 Mediator
