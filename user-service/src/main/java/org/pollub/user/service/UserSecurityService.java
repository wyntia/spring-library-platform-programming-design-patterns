package org.pollub.user.service;

import lombok.RequiredArgsConstructor;
import org.pollub.common.adapter.IPasswordGenerator;
import org.pollub.common.exception.UserNotFoundException;
import org.pollub.user.dto.ApiTextResponse;
import org.pollub.user.dto.ChangePasswordDto;
import org.pollub.user.dto.ResetPasswordRequestDto;
import org.pollub.user.dto.ResetPasswordResponseDto;
import org.pollub.user.model.User;
import org.pollub.user.repository.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
//Lab4 - SRP 1 Start
//Lab5 : ISP 1 Start
public class UserSecurityService implements IUserSecurityService {
//Lab5 : ISP 1 End
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IPasswordGenerator passwordGenerator;
    private final UserEventPublisher userEventPublisher;

    @Transactional
    public ApiTextResponse changePassword(String username, ChangePasswordDto passwordDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (passwordEncoder.matches(passwordDto.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Nowe hasło nie może być takie samo jak dotychczasowe.");
        }

        if (!passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Błąd przy zmianie hasła. Proszę zweryfikuj wpisane hasło.");
        }

        String encodedPassword = passwordEncoder.encode(passwordDto.getNewPassword());
        user.setPassword(encodedPassword);
        user.setMustChangePassword(false);

        userRepository.save(user);
        userEventPublisher.publish("PASSWORD_CHANGED", user, "User password changed");

        return new ApiTextResponse(true, "Password for user " + username + " changed successfully");
    }

    public User validateCredentials(String usernameOrEmail, String password) {
        User user = userRepository.findByEmail(usernameOrEmail)
                .or(() -> userRepository.findByUsername(usernameOrEmail.toLowerCase()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return user;
    }

    @Transactional
    public ResetPasswordResponseDto resetPassword(ResetPasswordRequestDto request) {
        User user = userRepository.findByEmailAndPesel(request.getEmail(), request.getPesel())
                .orElse(null);

        if (user == null) {
            return ResetPasswordResponseDto.builder()
                    .success(false)
                    .message("Jeśli podane dane są poprawne, nowe hasło zostanie wysłane na podany adres email.")
                    .build();
        }

        String temporaryPassword = passwordGenerator.generate();

        String encodedPassword = passwordEncoder.encode(temporaryPassword);
        user.setPassword(encodedPassword);
        user.setMustChangePassword(true);
        userRepository.save(user);

        userEventPublisher.publish("PASSWORD_RESET", user, "User password reset with temporary password");

        return ResetPasswordResponseDto.builder()
                .email(user.getEmail())
                .temporaryPassword(temporaryPassword)
                .success(true)
                .message("Nowe hasło zostało wygenerowane i zostanie wysłane na podany adres email.")
                .build();
    }
}
//SRP1 End
