package org.pollub.catalog.command;

import org.pollub.catalog.service.IBookService;

//start L5 Command     
public class DeleteBookCommand implements Command<Void> {
    private final IBookService bookService;
    private final Long id;

    public DeleteBookCommand(IBookService bookService, Long id) {
        this.bookService = bookService;
        this.id = id;
    }

    @Override
    public Void execute() {
        bookService.deleteBook(id);
        return null;
    }
}
//end L5 Command