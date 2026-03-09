package org.pollub.catalog.visitor;

import org.pollub.catalog.model.Book;
import org.pollub.catalog.model.LibraryItem;
import org.pollub.catalog.model.MovieDisc;
import org.pollub.catalog.service.IBranchInventoryService;
import org.pollub.common.dto.ItemDto;
import java.util.List;

public class ItemDtoMappingVisitor implements LibraryItemVisitor {
    private final IBranchInventoryService inventoryService;
    private ItemDto.ItemDtoBuilder builder;

    public ItemDtoMappingVisitor(IBranchInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    private void initBuilder(LibraryItem item) {
        List<Long> availableBranches = inventoryService.getAvailableBranchIds(item.getId());
        String overallStatus = availableBranches.isEmpty() ? "UNAVAILABLE" : "AVAILABLE";

        this.builder = ItemDto.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .imageUrl(item.getImageUrl())
                .itemType(item.getItemType().name())
                .status(overallStatus)
                .releaseYear(item.getReleaseYear())
                .isBestseller(item.getIsBestseller());
    }

    @Override
    public void visit(Book book) {
        initBuilder(book);
        builder.author(book.getAuthor())
                .isbn(book.getIsbn())
                .pageCount(book.getPageCount());
    }

    @Override
    public void visit(MovieDisc movie) {
        initBuilder(movie);
        builder.director(movie.getDirector())
                .durationMinutes(movie.getDuration());
    }

    public ItemDto getResult() {
        return builder.build();
    }
}