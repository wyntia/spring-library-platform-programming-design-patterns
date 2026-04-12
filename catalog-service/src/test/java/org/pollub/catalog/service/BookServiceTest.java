package org.pollub.catalog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pollub.catalog.model.Book;
import org.pollub.catalog.model.ItemType;
import org.pollub.catalog.model.dto.BookCreateDto;
import org.pollub.catalog.repository.IBranchInventoryRepository;
import org.pollub.catalog.repository.IBookRepository;
import org.pollub.common.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

/**
 * Lab10 – Testy jednostkowe dla klasy 3: BookService
 * Zgodne z zaleceniami ISTQB:
 *  - Jednoznaczne przypadki testowe (pozytywne i negatywne)
 *  - Izolacja SUT przez mockowanie repozytoriów
 *  - Testowanie warunków brzegowych (brak książki, usunięcie)
 */
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private IBookRepository bookRepository;

    @Mock
    private IBranchInventoryRepository inventoryRepository;

    @InjectMocks
    private BookService bookService;

    private Book sampleBook;
    private BookCreateDto sampleDto;

    @BeforeEach
    void setUp() {
        sampleBook = Book.builder()
                .title("Effective Java")
                .author("Joshua Bloch")
                .isbn("978-0-13-468599-1")
                .pageCount(412)
                .paperType("Paperback")
                .publisher("Addison-Wesley")
                .shelfNumber(5)
                .genre("Programming")
                .itemType(ItemType.BOOK)
                .build();
        sampleBook.setId(10L);

        sampleDto = BookCreateDto.builder()
                .title("Effective Java")
                .author("Joshua Bloch")
                .isbn("978-0-13-468599-1")
                .pageCount(412)
                .paperType("Paperback")
                .publisher("Addison-Wesley")
                .shelfNumber(5)
                .genre("Programming")
                .build();
    }

    // -------------------------------------------------------------------------
    // Lab10 Test 1 dla klasy 3 Start
    @Test
    @DisplayName("[TC-B1] findById – istniejąca książka jest zwrócona")
    void findById_existingBook_returnsBook() {
        // Arrange
        given(bookRepository.findById(10L)).willReturn(Optional.of(sampleBook));

        // Act
        Book result = bookService.findById(10L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Effective Java");
        assertThat(result.getAuthor()).isEqualTo("Joshua Bloch");
        then(bookRepository).should(times(1)).findById(10L);
    }
    // Lab10 Test 1 dla klasy 3 End

    // -------------------------------------------------------------------------
    // Lab10 Test 2 dla klasy 3 Start
    @Test
    @DisplayName("[TC-B2] findById – nieistniejąca książka rzuca ResourceNotFoundException")
    void findById_nonExistingBook_throwsResourceNotFoundException() {
        // Arrange
        given(bookRepository.findById(999L)).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> bookService.findById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }
    // Lab10 Test 2 dla klasy 3 End

    // -------------------------------------------------------------------------
    // Lab10 Test 3 dla klasy 3 Start
    @Test
    @DisplayName("[TC-B3] createBook – DTO jest mapowane na obiekt Book i zapisywane")
    void createBook_validDto_savesAndReturnsBook() {
        // Arrange
        given(bookRepository.save(any(Book.class))).willReturn(sampleBook);

        // Act
        Book result = bookService.createBook(sampleDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getIsbn()).isEqualTo("978-0-13-468599-1");
        then(bookRepository).should(times(1)).save(any(Book.class));
    }
    // Lab10 Test 3 dla klasy 3 End

    // -------------------------------------------------------------------------
    // Lab10 Test 4 dla klasy 3 Start
    @Test
    @DisplayName("[TC-B4] deleteBook – istniejąca książka jest usuwana")
    void deleteBook_existingBook_invokesRepositoryDelete() {
        // Arrange
        given(bookRepository.existsById(10L)).willReturn(true);
        willDoNothing().given(bookRepository).deleteById(10L);

        // Act
        bookService.deleteBook(10L);

        // Assert
        then(bookRepository).should(times(1)).deleteById(10L);
    }
    // Lab10 Test 4 dla klasy 3 End

    // -------------------------------------------------------------------------
    // Lab10 Test 5 dla klasy 3 Start
    @Test
    @DisplayName("[TC-B5] deleteBook – nieistniejąca książka rzuca ResourceNotFoundException")
    void deleteBook_nonExistingBook_throwsResourceNotFoundException() {
        // Arrange
        given(bookRepository.existsById(777L)).willReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> bookService.deleteBook(777L))
                .isInstanceOf(ResourceNotFoundException.class);
        then(bookRepository).should(never()).deleteById(anyLong());
    }
    // Lab10 Test 5 dla klasy 3 End
}
