package org.pollub.feedback.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pollub.feedback.repository.IFeedbackRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

/**
 * Lab10 – Testy jednostkowe dla klasy 2: FeedbackRateLimitService
 * Zgodne z zaleceniami ISTQB:
 *  - Izolacja z użyciem Mockito
 *  - Testowanie warunków brzegowych (null IP, puste IP)
 *  - Testowanie różnych ról (GUEST, AUTHENTICATED, ROLE_ADMIN)
 *  - Czyszczenie SecurityContext po każdym teście
 */
@ExtendWith(MockitoExtension.class)
class FeedbackRateLimitServiceTest {

    @Mock
    private IFeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackRateLimitService feedbackRateLimitService;

    @BeforeEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void resetSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    // -------------------------------------------------------------------------
    // Lab10 Test 1 dla klasy 2 Start
    @Test
    @DisplayName("[TC-F1] isRateLimitExceeded – null IP zawsze blokuje żądanie")
    void isRateLimitExceeded_nullIpAddress_returnsTrue() {
        // Arrange – brak kontekstu bezpieczeństwa (anonymous)

        // Act
        boolean result = feedbackRateLimitService.isRateLimitExceeded(null);

        // Assert
        assertThat(result).isTrue();
        // Repozytorium nie powinno być odpytywane jeśli IP jest null
        then(feedbackRepository).should(never()).countByIpAddressSince(any(), any());
    }
    // Lab10 Test 1 dla klasy 2 End

    // -------------------------------------------------------------------------
    // Lab10 Test 2 dla klasy 2 Start
    @Test
    @DisplayName("[TC-F2] isRateLimitExceeded – gość poniżej limitu (2 z 3) zwraca false")
    void isRateLimitExceeded_guestUnderLimit_returnsFalse() {
        // Arrange – brak uwierzytelnionego użytkownika → rule gusta (max=3, window=1h)
        given(feedbackRepository.countByIpAddressSince(eq("192.168.1.1"), any(LocalDateTime.class)))
                .willReturn(2L);

        // Act
        boolean result = feedbackRateLimitService.isRateLimitExceeded("192.168.1.1");

        // Assert
        assertThat(result).isFalse();
    }
    // Lab10 Test 2 dla klasy 2 End

    // -------------------------------------------------------------------------
    // Lab10 Test 3 dla klasy 2 Start
    @Test
    @DisplayName("[TC-F3] isRateLimitExceeded – gość osiągnął limit (3 z 3) zwraca true")
    void isRateLimitExceeded_guestAtLimit_returnsTrue() {
        // Arrange – rule gosta: max=3
        given(feedbackRepository.countByIpAddressSince(eq("10.0.0.1"), any(LocalDateTime.class)))
                .willReturn(3L);

        // Act
        boolean result = feedbackRateLimitService.isRateLimitExceeded("10.0.0.1");

        // Assert
        assertThat(result).isTrue();
    }
    // Lab10 Test 3 dla klasy 2 End

    // -------------------------------------------------------------------------
    // Lab10 Test 4 dla klasy 2 Start
    @Test
    @DisplayName("[TC-F4] isRateLimitExceeded – ROLE_ADMIN ma wyższy limit (19 z 20) zwraca false")
    void isRateLimitExceeded_adminUnderHigherLimit_returnsFalse() {
        // Arrange – uwierzytelniony użytkownik z rolą ROLE_ADMIN (max=20)
        UserDetails adminDetails = org.springframework.security.core.userdetails.User
                .withUsername("admin")
                .password("pass")
                .authorities("ROLE_ADMIN")
                .build();
        Authentication auth = new UsernamePasswordAuthenticationToken(
                adminDetails, null, adminDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        given(feedbackRepository.countByIpAddressSince(eq("172.16.0.1"), any(LocalDateTime.class)))
                .willReturn(19L);

        // Act
        boolean result = feedbackRateLimitService.isRateLimitExceeded("172.16.0.1");

        // Assert
        assertThat(result).isFalse();
    }
    // Lab10 Test 4 dla klasy 2 End

    // -------------------------------------------------------------------------
    // Lab10 Test 5 dla klasy 2 Start
    @Test
    @DisplayName("[TC-F5] getRateLimitInfo – dla gościa zwraca [aktualnaLiczba, 3, 1]")
    void getRateLimitInfo_guestUser_returnsCorrectArray() {
        // Arrange – brak uwierzytelnionego użytkownika → rule gosta (max=3, window=1h)
        given(feedbackRepository.countByIpAddressSince(eq("192.168.0.5"), any(LocalDateTime.class)))
                .willReturn(1L);

        // Act
        int[] info = feedbackRateLimitService.getRateLimitInfo("192.168.0.5");

        // Assert
        assertThat(info).hasSize(3);
        assertThat(info[0]).isEqualTo(1);   // bieżąca liczba
        assertThat(info[1]).isEqualTo(3);   // maxRequests dla gościa
        assertThat(info[2]).isEqualTo(1);   // windowHours
    }
    // Lab10 Test 5 dla klasy 2 End
}
