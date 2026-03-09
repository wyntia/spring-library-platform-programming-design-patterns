package org.pollub.catalog.visitor;

import org.pollub.catalog.model.Book;
import org.pollub.catalog.model.MovieDisc;

//L6 Visitor Design Pattern - Visitor interface for library items
public interface LibraryItemVisitor {
    void visit(Book book);
    void visit(MovieDisc movie);
}
