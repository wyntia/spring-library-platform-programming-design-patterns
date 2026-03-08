package org.pollub.catalog.mediator;

import org.pollub.catalog.model.Book;
import org.pollub.catalog.model.dto.BookCreateDto;
import org.pollub.catalog.model.SearchCriteria;
import org.pollub.catalog.model.dto.BookAvailabilityDto;
import org.springframework.data.domain.Page;
import java.util.List;

//start L5 Mediator
public interface BookMediator {
    Book findBookById(Long id);
    Book createBook(BookCreateDto dto);
    Book updateBook(Long id, BookCreateDto dto);
    void deleteBook(Long id);
    Page<Book> searchBooks(SearchCriteria criteria);
    BookAvailabilityDto getBookAvailability(Long id);
    List<Book> getRecentBooks(int limit);
    List<Book> getPopularBooks(int limit);
}
//end L5 Mediator
