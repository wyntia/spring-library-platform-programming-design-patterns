//start L2 Decorator
package org.pollub.catalog.service;

import lombok.extern.slf4j.Slf4j;
import org.pollub.catalog.model.Book;
import org.pollub.catalog.model.SearchCriteria;
import org.pollub.catalog.model.dto.BookAvailabilityDto;
import org.pollub.catalog.model.dto.BookCreateDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Supplier;

/**
 * Logging Decorator (wzorzec Decorator) dla IBookService.
 * Dodaje warstwę logowania czasu wykonania każdej operacji,
 * delegując wszystkie operacje do opakowanego serwisu.
 */
@Slf4j
public class BookServiceLoggingDecorator implements IBookService {
    private final IBookService delegate;

    public BookServiceLoggingDecorator(IBookService delegate) {
        this.delegate = delegate;
    }

    private <T> T logExecution(String methodName, Supplier<T> action) {
        log.info("BookService.{}() - START", methodName);
        long start = System.currentTimeMillis();
        T result = action.get();
        long duration = System.currentTimeMillis() - start;
        log.info("BookService.{}() - END ({}ms)", methodName, duration);
        return result;
    }

    private void logExecutionVoid(String methodName, Runnable action) {
        log.info("BookService.{}() - START", methodName);
        long start = System.currentTimeMillis();
        action.run();
        long duration = System.currentTimeMillis() - start;
        log.info("BookService.{}() - END ({}ms)", methodName, duration);
    }

    @Override
    public Book findById(Long id) {
        return logExecution("findById", () -> delegate.findById(id));
    }

    @Override
    public List<Book> findByTitle(String title) {
        return logExecution("findByTitle", () -> delegate.findByTitle(title));
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return logExecution("findByAuthor", () -> delegate.findByAuthor(author));
    }

    @Override
    public List<Book> findByGenre(String genre) {
        return logExecution("findByGenre", () -> delegate.findByGenre(genre));
    }

    @Override public List<Book> findAll() { return logExecution("findAll", () -> delegate.findAll()); }
    @Override public Page<Book> getBooksPaginated(int page, int size) { return logExecution("getBooksPaginated", () -> delegate.getBooksPaginated(page, size)); }
    @Override public Book createBook(BookCreateDto dto) { return logExecution("createBook", () -> delegate.createBook(dto)); }
    @Override public Book updateBook(Long id, BookCreateDto dto) { return logExecution("updateBook", () -> delegate.updateBook(id, dto)); }
    @Override public void deleteBook(Long id) { logExecutionVoid("deleteBook", () -> delegate.deleteBook(id)); }
    @Override public List<Book> findByIsbn(String isbn) { return logExecution("findByIsbn", () -> delegate.findByIsbn(isbn)); }
    @Override public Page<Book> searchBooks(SearchCriteria criteria) { return logExecution("searchBooks", () -> delegate.searchBooks(criteria)); }
    @Override public List<String> getTopGenres() { return logExecution("getTopGenres", () -> delegate.getTopGenres()); }
    @Override public List<String> getOtherGenres() { return logExecution("getOtherGenres", () -> delegate.getOtherGenres()); }
    @Override public List<String> getAllPublishers() { return logExecution("getAllPublishers", () -> delegate.getAllPublishers()); }
    @Override public List<String> getAllStatuses() { return logExecution("getAllStatuses", () -> delegate.getAllStatuses()); }
    @Override public List<Book> getRecentBooks(int limit) { return logExecution("getRecentBooks", () -> delegate.getRecentBooks(limit)); }
    @Override public List<Book> getPopularBooks(int limit) { return logExecution("getPopularBooks", () -> delegate.getPopularBooks(limit)); }
    @Override public BookAvailabilityDto getBookAvailability(Long id) { return logExecution("getBookAvailability", () -> delegate.getBookAvailability(id)); }
}
//end L2 Decorator
