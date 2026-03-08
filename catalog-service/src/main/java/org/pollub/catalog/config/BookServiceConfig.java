//start L3 Decorator Config
package org.pollub.catalog.config;

import org.pollub.catalog.service.BookService;
import org.pollub.catalog.service.BookServiceCachingDecorator;
import org.pollub.catalog.service.IBookService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for the BookService caching decorator.
 */
@Configuration
public class BookServiceConfig {
    @Bean
    @Primary
    public IBookService bookService(@Qualifier("baseBookService") BookService bookService) {
        return new BookServiceCachingDecorator(bookService);
    }
}
//end L3 Decorator Config
