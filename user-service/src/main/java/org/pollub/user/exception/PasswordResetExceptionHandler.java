package org.pollub.user.exception;

import org.pollub.user.dto.ResetPasswordResponseDto;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//Lab6 : Wyjątki zamiast kodów błędów — przykład 1 (reset hasła) Start
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PasswordResetExceptionHandler {

    private static final String GENERIC_RESET_MESSAGE =
            "Jeśli podane dane są poprawne, nowe hasło zostanie wysłane na podany adres email.";

    @ExceptionHandler(PasswordResetIdentityNotVerifiedException.class)
    public ResponseEntity<ResetPasswordResponseDto> handlePasswordResetIdentityNotVerified() {
        return ResponseEntity.ok(
                ResetPasswordResponseDto.builder()
                        .success(false)
                        .message(GENERIC_RESET_MESSAGE)
                        .build()
        );
    }
}
//Lab6 : Wyjątki zamiast kodów błędów — przykład 1 (reset hasła) Stop
