package org.pollub.catalog.command;

import org.pollub.catalog.model.dto.BookCreateDto;
import org.pollub.catalog.model.Book;
import org.pollub.catalog.service.IBookService;

//start L5 Command
public class UpdateBookCommand implements Command<Book> {
    private final IBookService bookService;
    private final Long id;
    private final BookCreateDto dto;

    public UpdateBookCommand(IBookService bookService, Long id, BookCreateDto dto) {
        this.bookService = bookService;
        this.id = id;
        this.dto = dto;
    }

    @Override
    public Book execute() {
        return bookService.updateBook(id, dto);
    }
}
//end L5 Command