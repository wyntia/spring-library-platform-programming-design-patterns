package org.pollub.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pollub.common.exception.ResourceNotFoundException;
import org.pollub.user.dto.UserEventSnapshot;
import org.pollub.user.model.Role;
import org.pollub.user.model.User;
import org.pollub.user.model.UserAddress;
import org.pollub.user.repository.IUserRepository;
import org.pollub.user.service.search.UserSearchExpressionFactory;
import org.pollub.user.service.utils.IUserFactory;
import org.pollub.user.service.utils.UserValidator;
import org.pollub.user.service.utils.exporter.IProfileExporter;
import org.pollub.user.service.utils.logger.IUserActivityLogger;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

/**
 * Lab10 – Testy jednostkowe dla klasy 1: UserProfileService
 * Zgodne z zaleceniami ISTQB:
 *  - Izolacja jednostki testowanej (mocki zależności)
 *  - Każdy test weryfikuje jeden warunek
 *  - Czytelne opisy (@DisplayName)
 *  - Pokrycie: ścieżki pozytywne i negatywne
 */
@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock private IUserRepository userRepository;
    @Mock private IUserFactory userFactory;
    @Mock private UserValidator userValidator;
    @Mock private UserEventPublisher userEventPublisher;
    @Mock private List<UserSearchExpressionFactory> userSearchExpressionFactories;
    @Mock private IUserActivityLogger activityLogger;
    @Mock private IProfileExporter profileExporter;

    @InjectMocks
    private UserProfileService userProfileService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(1L)
                .username("jkowalski")
                .email("j.kowalski@pollub.pl")
                .password("hashed")
                .name("Jan")
                .surname("Kowalski")
                .enabled(true)
                .roles(Set.of(Role.ROLE_READER))
                .build();
    }

    // -------------------------------------------------------------------------
    // Lab10 Test 1 dla klasy 1 Start
    @Test
    @DisplayName("[TC-U1] findById – istniejący użytkownik zostaje zwrócony")
    void findById_existingUser_returnsUser() {
        // Arrange (Given)
        given(userRepository.findById(1L)).willReturn(Optional.of(sampleUser));

        // Act (When)
        User result = userProfileService.findById(1L);

        // Assert (Then)
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("jkowalski");
        then(userRepository).should(times(1)).findById(1L);
    }
    // Lab10 Test 1 dla klasy 1 End

    // -------------------------------------------------------------------------
    // Lab10 Test 2 dla klasy 1 Start
    @Test
    @DisplayName("[TC-U2] findById – nieistniejący użytkownik rzuca ResourceNotFoundException")
    void findById_nonExistingUser_throwsResourceNotFoundException() {
        // Arrange
        given(userRepository.findById(99L)).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userProfileService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
        then(userRepository).should(times(1)).findById(99L);
    }
    // Lab10 Test 2 dla klasy 1 End

    // -------------------------------------------------------------------------
    // Lab10 Test 3 dla klasy 1 Start
    @Test
    @DisplayName("[TC-U3] findByUsername – użytkownik wyszukiwany jest po nazwie (lowercase)")
    void findByUsername_existingUsername_returnsUser() {
        // Arrange
        given(userRepository.findByUsername("jkowalski")).willReturn(Optional.of(sampleUser));

        // Act
        User result = userProfileService.findByUsername("JKOWALSKI");

        // Assert
        assertThat(result.getUsername()).isEqualTo("jkowalski");
        // username is converted to lowercase before query
        then(userRepository).should().findByUsername("jkowalski");
    }
    // Lab10 Test 3 dla klasy 1 End

    // -------------------------------------------------------------------------
    // Lab10 Test 4 dla klasy 1 Start
    @Test
    @DisplayName("[TC-U4] createUser – nowy użytkownik jest walidowany, tworzony i zapisywany")
    void createUser_validUser_savesAndReturnsUser() {
        // Arrange
        given(userFactory.createUser(sampleUser)).willReturn(sampleUser);
        given(userRepository.save(sampleUser)).willReturn(sampleUser);
        willDoNothing().given(userValidator).validateNewUser(sampleUser);
        willDoNothing().given(userEventPublisher).publish(anyString(), any(User.class), anyString());

        // Act
        User result = userProfileService.createUser(sampleUser);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("j.kowalski@pollub.pl");
        then(userValidator).should().validateNewUser(sampleUser);
        then(userFactory).should().createUser(sampleUser);
        then(userRepository).should().save(sampleUser);
        then(userEventPublisher).should().publish(eq("USER_CREATED"), eq(sampleUser), anyString());
    }
    // Lab10 Test 4 dla klasy 1 End

    // -------------------------------------------------------------------------
    // Lab10 Test 5 dla klasy 1 Start
    @Test
    @DisplayName("[TC-U5] deleteUser – usunięcie nieistniejącego użytkownika rzuca ResourceNotFoundException")
    void deleteUser_nonExistingUser_throwsResourceNotFoundException() {
        // Arrange
        given(userRepository.existsById(42L)).willReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> userProfileService.deleteUser(42L))
                .isInstanceOf(ResourceNotFoundException.class);
        then(userRepository).should(never()).deleteById(anyLong());
    }
    // Lab10 Test 5 dla klasy 1 End
}
