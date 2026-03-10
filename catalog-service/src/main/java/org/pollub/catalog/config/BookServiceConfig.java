//start L2 Decorator Config
package org.pollub.catalog.config;

import org.pollub.catalog.service.BookService;
import org.pollub.catalog.service.BookServiceLoggingDecorator;
import org.pollub.catalog.service.IBookService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the BookService logging decorator.
 */
@Configuration
public class BookServiceConfig {
    @Bean
    public IBookService loggingBookService(@Qualifier("baseBookService") BookService bookService) {
        return new BookServiceLoggingDecorator(bookService);
    }
}
//end L2 Decorator Config
