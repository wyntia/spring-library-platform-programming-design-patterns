//start L2 Composite
package org.pollub.catalog.controller;

import org.pollub.catalog.model.Book;
import org.pollub.catalog.service.BookService;
import org.pollub.catalog.service.CategoryCompositeBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller demonstrating the use of the Composite design pattern 
 * to represent a hierarchical category tree based on book genres.
 */
@RestController
@RequestMapping("/api/categories/composite")
public class CategoryCompositeController {
    private final BookService bookService;
    public CategoryCompositeController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public CategoryCompositeBuilder.CategoryComponent getCategoryTree() {
        List<Book> books = bookService.findAll();
        return CategoryCompositeBuilder.buildCategoryTree(books);
    }
}
//end L2 Composite Example
