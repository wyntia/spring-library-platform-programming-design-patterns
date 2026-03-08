package org.pollub.catalog.mediator;

import lombok.RequiredArgsConstructor;
import org.pollub.catalog.model.Book;
import org.pollub.catalog.model.dto.BookCreateDto;
import org.pollub.catalog.model.SearchCriteria;
import org.pollub.catalog.model.dto.BookAvailabilityDto;
import org.pollub.catalog.service.IBookService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.pollub.catalog.command.Command;
import org.pollub.catalog.command.CreateBookCommand;
import org.pollub.catalog.command.UpdateBookCommand;
import org.pollub.catalog.command.DeleteBookCommand;
import java.util.List;

//start L5 Mediator
@Component
@RequiredArgsConstructor
public class BookMediatorImpl implements BookMediator {
    private final IBookService bookService;

    @Override
    public Book findBookById(Long id) {
        return bookService.findById(id);
    }

    @Override
    public Book createBook(BookCreateDto dto) {
        // start L5 Command
        Command<Book> command = new CreateBookCommand(bookService, dto);
        return command.execute();
        // end L5 Command
    }

    @Override
    public Book updateBook(Long id, BookCreateDto dto) {
        Command<Book> command = new UpdateBookCommand(bookService, id, dto);
        return command.execute();
    }

    @Override
    public void deleteBook(Long id) {
        Command<Void> command = new DeleteBookCommand(bookService, id);
        command.execute();
    }

    @Override
    public Page<Book> searchBooks(SearchCriteria criteria) {
        return bookService.searchBooks(criteria);
    }

    @Override
    public BookAvailabilityDto getBookAvailability(Long id) {
        return bookService.getBookAvailability(id);
    }

    @Override
    public List<Book> getRecentBooks(int limit) {
        return bookService.getRecentBooks(limit);
    }

    @Override
    public List<Book> getPopularBooks(int limit) {
        return bookService.getPopularBooks(limit);
    }
}
//end L5 Mediator
