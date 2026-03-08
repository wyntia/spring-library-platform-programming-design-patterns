package org.pollub.catalog.command;

import org.pollub.catalog.model.dto.BookCreateDto;
import org.pollub.catalog.model.Book;
import org.pollub.catalog.service.IBookService;

//start L5 Command
public class CreateBookCommand implements Command<Book> {
    private final IBookService bookService;
    private final BookCreateDto dto;

    public CreateBookCommand(IBookService bookService, BookCreateDto dto) {
        this.bookService = bookService;
        this.dto = dto;
    }

    @Override
    public Book execute() {
        return bookService.createBook(dto);
    }
}
//end L5 Command