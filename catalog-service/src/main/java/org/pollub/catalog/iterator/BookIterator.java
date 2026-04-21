package org.pollub.catalog.iterator;

import org.pollub.catalog.model.Book;
import java.util.Iterator;
import java.util.List;

//start L3 Iterator
public class BookIterator implements Iterator<Book> {
    private final List<Book> books;
    private int position = 0;

    public BookIterator(List<Book> books) {
        this.books = books;
    }

    @Override
    public boolean hasNext() {
        return position < books.size();
    }

    @Override
    public Book next() {
        return books.get(position++);
    }
}
//end L3 Iterator
