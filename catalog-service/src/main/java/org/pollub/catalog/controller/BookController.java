package org.pollub.catalog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pollub.catalog.model.Book;
import org.pollub.catalog.model.ItemStatus;
import org.pollub.catalog.model.dto.BookAvailabilityDto;
import org.pollub.catalog.model.dto.BookCreateDto;
import org.pollub.catalog.model.SearchCriteria;
import org.pollub.catalog.service.IBookService;
import org.pollub.catalog.command.CreateBookCommand;
import org.pollub.catalog.command.UpdateBookCommand;
import org.pollub.catalog.command.DeleteBookCommand;
import org.pollub.catalog.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items/book")
@RequiredArgsConstructor
public class BookController {
    private final IBookService bookService;
    //start L5 Mediator
    private final org.pollub.catalog.mediator.BookMediator bookMediator;
    //end L5 Mediator
    private final Logger log = LoggerFactory.getLogger(BookController.class);

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/pagination")
    public Page<Book> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "16") int size) {

        if (size > 16) {
            size = 16;
        }
        return bookService.getBooksPaginated(page, size);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<Book> createBook(@Valid @RequestBody BookCreateDto dto) {
        //start L5 Mediator
        Book book = bookMediator.createBook(dto);
        ResponseEntity<Book> response = ResponseEntity.ok(book);
        //end L5 Mediator
        return response;
    }
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        //start L5 Mediator
        return ResponseEntity.ok(bookMediator.findBookById(id));
        //end L5 Mediator
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable String author) {
        return ResponseEntity.ok(bookService.findByAuthor(author));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<Book>> getBooksByTitle(@PathVariable String title) {
        return ResponseEntity.ok(bookService.findByTitle(title));
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Book>> getBooksByGenre(@PathVariable String genre) {
        return ResponseEntity.ok(bookService.findByGenre(genre));
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<List<Book>> getBookByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(bookService.findByIsbn(isbn));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookCreateDto dto
    ) {
        //start L5 Mediator
        Book book = bookMediator.updateBook(id, dto);
        ResponseEntity<Book> response = ResponseEntity.ok(book);
        //end L5 Mediator
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        //start L5 Mediator
        bookMediator.deleteBook(id);
        ResponseEntity<String> response = ResponseEntity.ok("Book deleted");
        //end L5 Mediator
        return response;
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Book>> searchBooks(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) ItemStatus status,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) String genres,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "16") int size,
            @RequestParam(required = false) String sort
    ) {
        if (size > 16) {
            size = 16;
        }

        //Lab1 - Builder 3 Start
        SearchCriteria criteria = SearchCriteria.builder()
                .query(query)
                .status(status)
                .publisher(publisher)
                .genres(genres)
                .page(page)
                .size(size)
                .sort(sort)
                .build();
        //Lab1 - Builder 3 End

        this.log.debug("Searching books with criteria: {}", criteria);
        //start L5 Mediator
        return ResponseEntity.ok(bookMediator.searchBooks(criteria));
        //end L5 Mediator
    }
    @GetMapping("/genres")
    public ResponseEntity<List<String>> getTopGenres() {
        return ResponseEntity.ok(bookService.getTopGenres());
    }

    @GetMapping("/genres/other")
    public ResponseEntity<List<String>> getOtherGenres() {
        return ResponseEntity.ok(bookService.getOtherGenres());
    }

    @GetMapping("/publishers")
    public ResponseEntity<List<String>> getAllPublishers() {
        return ResponseEntity.ok(bookService.getAllPublishers());
    }

    @GetMapping("/statuses")
    public ResponseEntity<List<String>> getAllStatuses() {
        return ResponseEntity.ok(bookService.getAllStatuses());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Book>> getRecentBooks(
            @RequestParam(defaultValue = "7") int limit) {
        if (limit > 20) {
            limit = 20;
        }
        //start L5 Mediator
        return ResponseEntity.ok(bookMediator.getRecentBooks(limit));
        //end L5 Mediator
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Book>> getPopularBooks(
            @RequestParam(defaultValue = "10") int limit) {
        if (limit > 20) {
            limit = 20;
        }
        //start L5 Mediator
        return ResponseEntity.ok(bookMediator.getPopularBooks(limit));
        //end L5 Mediator
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<BookAvailabilityDto> getBookAvailability(
            @PathVariable Long id) {
        //start L5 Mediator
        return ResponseEntity.ok(bookMediator.getBookAvailability(id));
        //end L5 Mediator
    }


}
