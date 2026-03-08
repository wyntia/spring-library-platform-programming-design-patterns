package org.pollub.catalog.mediator;

import org.pollub.catalog.model.LibraryItem;
import java.util.List;

//start L5 Mediator
import org.pollub.catalog.model.dto.HistoryCatalogResponse;
import java.util.Map;

public interface ItemMediator {
    List<LibraryItem> findAll();
    LibraryItem findById(Long id);
    List<LibraryItem> findAvailable();
    List<LibraryItem> findRented();
    List<LibraryItem> findByBranchId(Long branchId);
    List<LibraryItem> findAvailableByBranch(Long branchId);
    List<LibraryItem> searchItems(String query);
    List<LibraryItem> findBestsellers();
    void deleteItem(Long id);

    Map<Long, HistoryCatalogResponse> getHistoryCatalogDataByItemIds(List<Long> itemIds);
}
//end L5 Mediator
