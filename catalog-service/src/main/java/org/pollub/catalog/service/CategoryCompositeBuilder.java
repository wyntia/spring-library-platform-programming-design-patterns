//start L3 Composite
package org.pollub.catalog.service;

import org.pollub.catalog.model.Book;
import java.util.*;

/**
 * Builder utility for creating a hierarchical category tree (Composite pattern)
 * based on the genre field of Book objects.
 */
public class CategoryCompositeBuilder {
    public static class CategoryComponent {
        private final String name;
        private final List<CategoryComponent> children = new ArrayList<>();
        private final List<Book> books = new ArrayList<>();
        public CategoryComponent(String name) { this.name = name; }
        public String getName() { return name; }
        public List<CategoryComponent> getChildren() { return children; }
        public List<Book> getBooks() { return books; }
        public void addChild(CategoryComponent child) { children.add(child); }
        public void addBook(Book book) { books.add(book); }
    }

   /**
     * Constructs a category tree from a list of books.
     * Groups books into categories based on their genre.
     * Example: Root -> Fiction -> [Book1, Book2]
     */
    public static CategoryComponent buildCategoryTree(List<Book> books) {
        Map<String, CategoryComponent> genreMap = new HashMap<>();
        CategoryComponent root = new CategoryComponent("All Books");
        for (Book book : books) {
            String genre = book.getGenre();
            if (genre == null) genre = "Unknown";
            CategoryComponent category = genreMap.computeIfAbsent(genre, CategoryComponent::new);
            category.addBook(book);
            if (!root.getChildren().contains(category)) {
                root.addChild(category);
            }
        }
        return root;
    }
}
//end L3 Composite
