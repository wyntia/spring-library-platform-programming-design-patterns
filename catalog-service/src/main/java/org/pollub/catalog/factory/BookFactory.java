package org.pollub.catalog.factory;

import org.pollub.catalog.model.Book;
import org.pollub.catalog.model.ItemType;
import org.pollub.catalog.model.LibraryItem;
import org.springframework.stereotype.Component;

//Lab2 - Factory 1 Method Start
/**
 * Factory Method implementation for creating Book objects.
 * Uses Book.builder() (Builder pattern) internally.
 */
@Component
public class BookFactory implements LibraryItemFactory {

    // Lab2 - Prototype Start
    private final Book templateBook;

    public BookFactory() {
        this.templateBook = Book.builder()
                .itemType(ItemType.BOOK)
                .build();
    }
    // End Prototype

    @Override
    public LibraryItem createItem(String title, String author, String genre, String description,
                                  String imageUrl, Integer releaseYear, Boolean isBestseller) {
        //Lab2 - Prototype 1 Start
        //Lab2 - Builder 1 Start
        return templateBook.clone().toBuilder()
                .title(title)
                .author(author)
                .genre(genre)
                .description(description)
                .imageUrl(imageUrl)
                .releaseYear(releaseYear)
                .isBestseller(isBestseller)
                .build();
        // End 1 Prototype
        // End 1 Builder
    }

    /**
     * Creates a Book with all specific fields using Builder pattern.
     */
    public Book createBook(String title, String author, String genre, String isbn,
                           Integer pageCount, String paperType, String publisher,
                           Integer shelfNumber, String description, String imageUrl,
                           Integer releaseYear, boolean isBestseller) {
        //Lab2 - Builder 1 Start
        //Lab2 - Prototype 1 Start
        return templateBook.clone().toBuilder()
                .title(title)
                .author(author)
                .genre(genre)
                .isbn(isbn)
                .pageCount(pageCount != null ? pageCount : 0)
                .paperType(paperType)
                .publisher(publisher)
                .shelfNumber(shelfNumber)
                .description(description)
                .imageUrl(imageUrl)
                .releaseYear(releaseYear)
                .isBestseller(isBestseller)
                .itemType(ItemType.BOOK)
                .build();
        // End 1 Builder
        // End Prototype 1
    }

    @Override
    public ItemType getItemType() {
        return ItemType.BOOK;
    }
}
// End Factory 1 Method
