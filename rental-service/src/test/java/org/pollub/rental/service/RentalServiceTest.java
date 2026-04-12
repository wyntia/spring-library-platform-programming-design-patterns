package org.pollub.rental.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pollub.common.mediator.Mediator;
import org.pollub.rental.bridge.IValidationBridge;
import org.pollub.rental.model.RentalHistory;
import org.pollub.rental.model.RentalStatus;
import org.pollub.rental.repository.IRentalHistoryRepository;
import org.pollub.rental.utils.IRentalValidator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

/**
 * Lab10 – Testy jednostkowe dla klasy 4: RentalService
 * Zgodne z zaleceniami ISTQB:
 *  - Pełna izolacja SUT przez mocki
 *  - Testy strategii obliczania opłat (LSP)
 *  - Testy warunków brzegowych (dni <= 0, null strategia)
 *  - Weryfikacja wywołań repozytoriów i obserwatorów
 */
@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock private IRentalHistoryRepository rentalHistoryRepository;
    @Mock private IRentalValidator rentalValidator;
    @Mock private IValidationBridge validationBridge;
    @Mock private Mediator mediator;

    @InjectMocks
    private RentalService rentalService;

    private RentalHistory activeRental;

    @BeforeEach
    void setUp() {
        activeRental = RentalHistory.builder()
                .id(100L)
                .itemId(5L)
                .userId(2L)
                .branchId(1L)
                .status(RentalStatus.RENTED)
                .rentedAt(LocalDateTime.now().minusDays(5))
                .dueDate(LocalDateTime.now().plusDays(9))
                .isExtended(false)
                .build();
    }

    // -------------------------------------------------------------------------
    // Lab10 Test 1 dla klasy 4 Start
    @Test
    @DisplayName("[TC-R1] calculateRentalFee – StandardFeeStrategy: 10 dni = 25.00 PLN")
    void calculateRentalFee_standardStrategy_returnsCorrectFee() {
        // Arrange
        IRentalFeeStrategy strategy = new StandardFeeStrategy();

        // Act
        BigDecimal fee = rentalService.calculateRentalFee(10, strategy);

        // Assert
        assertThat(fee).isEqualByComparingTo(new BigDecimal("25.00"));
    }
    // Lab10 Test 1 dla klasy 4 End

    // -------------------------------------------------------------------------
    // Lab10 Test 2 dla klasy 4 Start
    @Test
    @DisplayName("[TC-R2] calculateRentalFee – StudentDiscountFeeStrategy: 10 dni = 20.00 PLN (20% rabatu)")
    void calculateRentalFee_studentStrategy_appliesDiscount() {
        // Arrange
        IRentalFeeStrategy strategy = new StudentDiscountFeeStrategy();

        // Act
        BigDecimal fee = rentalService.calculateRentalFee(10, strategy);

        // Assert
        // 2.50 * 10 * 0.80 = 20.00
        assertThat(fee).isEqualByComparingTo(new BigDecimal("20.00"));
    }
    // Lab10 Test 2 dla klasy 4 End

    // -------------------------------------------------------------------------
    // Lab10 Test 3 dla klasy 4 Start
    @Test
    @DisplayName("[TC-R3] calculateRentalFee – zerowa liczba dni zwraca 0.00 PLN")
    void calculateRentalFee_zeroDays_returnsZero() {
        // Arrange
        IRentalFeeStrategy strategy = new StandardFeeStrategy();

        // Act
        BigDecimal fee = rentalService.calculateRentalFee(0, strategy);

        // Assert
        assertThat(fee).isEqualByComparingTo(BigDecimal.ZERO);
    }
    // Lab10 Test 3 dla klasy 4 End

    // -------------------------------------------------------------------------
    // Lab10 Test 4 dla klasy 4 Start
    @Test
    @DisplayName("[TC-R4] calculateRentalFee – null strategia rzuca IllegalArgumentException")
    void calculateRentalFee_nullStrategy_throwsIllegalArgumentException() {
        // Act & Assert
        assertThatThrownBy(() -> rentalService.calculateRentalFee(5, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("null");
    }
    // Lab10 Test 4 dla klasy 4 End

    // -------------------------------------------------------------------------
    // Lab10 Test 5 dla klasy 4 Start
    @Test
    @DisplayName("[TC-R5] getUserRentalHistory – zwraca historię wypożyczeń użytkownika")
    void getUserRentalHistory_existingUser_returnsHistory() {
        // Arrange
        given(rentalHistoryRepository.findByUserIdOrderByReturnedAtDesc(2L))
                .willReturn(List.of(activeRental));

        // Act
        List<RentalHistory> history = rentalService.getUserRentalHistory(2L);

        // Assert
        assertThat(history).isNotNull().hasSize(1);
        assertThat(history.get(0).getUserId()).isEqualTo(2L);
        assertThat(history.get(0).getStatus()).isEqualTo(RentalStatus.RENTED);
        then(rentalHistoryRepository).should(times(1))
                .findByUserIdOrderByReturnedAtDesc(2L);
    }
    // Lab10 Test 5 dla klasy 4 End
}
