//start L2 Decorator
package org.pollub.catalog.service;

import org.pollub.catalog.model.Book;
import org.pollub.catalog.model.SearchCriteria;
import org.pollub.catalog.model.dto.BookAvailabilityDto;
import org.pollub.catalog.model.dto.BookCreateDto;
import org.springframework.data.domain.Page;

import java.util.*;

/**
 * Caching implementation of the IBookService decorator.
 * Provides an in-memory cache for book lookup and search operations.
 */
public class BookServiceCachingDecorator implements IBookService {
    private final IBookService delegate;
    private final Map<Long, Book> bookCache = new HashMap<>();
    private final Map<String, List<Book>> searchCache = new HashMap<>();

    public BookServiceCachingDecorator(IBookService delegate) {
        this.delegate = delegate;
    }

    @Override
    public Book findById(Long id) {
        if (bookCache.containsKey(id)) {
            return bookCache.get(id);
        }
        Book book = delegate.findById(id);
        bookCache.put(id, book);
        return book;
    }

    @Override
    public List<Book> findByTitle(String title) {
        String key = "title:" + title;
        if (searchCache.containsKey(key)) {
            return searchCache.get(key);
        }
        List<Book> result = delegate.findByTitle(title);
        searchCache.put(key, result);
        return result;
    }

    @Override
    public List<Book> findByAuthor(String author) {
        String key = "author:" + author;
        if (searchCache.containsKey(key)) {
            return searchCache.get(key);
        }
        List<Book> result = delegate.findByAuthor(author);
        searchCache.put(key, result);
        return result;
    }

    @Override
    public List<Book> findByGenre(String genre) {
        String key = "genre:" + genre;
        if (searchCache.containsKey(key)) {
            return searchCache.get(key);
        }
        List<Book> result = delegate.findByGenre(genre);
        searchCache.put(key, result);
        return result;
    }

    @Override public List<Book> findAll() { return delegate.findAll(); }
    @Override public Page<Book> getBooksPaginated(int page, int size) { return delegate.getBooksPaginated(page, size); }
    @Override public Book createBook(BookCreateDto dto) { return delegate.createBook(dto); }
    @Override public Book updateBook(Long id, BookCreateDto dto) { return delegate.updateBook(id, dto); }
    @Override public void deleteBook(Long id) { delegate.deleteBook(id); bookCache.remove(id); }
    @Override public List<Book> findByIsbn(String isbn) { return delegate.findByIsbn(isbn); }
    @Override public Page<Book> searchBooks(SearchCriteria criteria) { return delegate.searchBooks(criteria); }
    @Override public List<String> getTopGenres() { return delegate.getTopGenres(); }
    @Override public List<String> getOtherGenres() { return delegate.getOtherGenres(); }
    @Override public List<String> getAllPublishers() { return delegate.getAllPublishers(); }
    @Override public List<String> getAllStatuses() { return delegate.getAllStatuses(); }
    @Override public List<Book> getRecentBooks(int limit) { return delegate.getRecentBooks(limit); }
    @Override public List<Book> getPopularBooks(int limit) { return delegate.getPopularBooks(limit); }
    @Override public BookAvailabilityDto getBookAvailability(Long id) { return delegate.getBookAvailability(id); }
}
//end L2 Decorator
