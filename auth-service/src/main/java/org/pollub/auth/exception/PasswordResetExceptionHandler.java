package org.pollub.auth.exception;

import org.pollub.auth.dto.ResetPasswordResponseDto;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//Lab6 : Wyjątki zamiast kodów błędów — przykład 1 (reset hasła) Start
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PasswordResetExceptionHandler {

    private static final String USER_SERVICE_FAILURE_MESSAGE =
            "Jeśli podane dane są poprawne, nowe hasło zostanie wysłane na podany adres email.";
    private static final String EMAIL_FAILURE_MESSAGE =
            "Wystąpił błąd podczas wysyłania emaila. Spróbuj ponownie później.";

    @ExceptionHandler(PasswordResetUserServiceFailedException.class)
    public ResponseEntity<ResetPasswordResponseDto> handleUserServiceFailed() {
        return ResponseEntity.ok(
                ResetPasswordResponseDto.builder()
                        .success(false)
                        .message(USER_SERVICE_FAILURE_MESSAGE)
                        .build()
        );
    }

    @ExceptionHandler(PasswordResetEmailDeliveryException.class)
    public ResponseEntity<ResetPasswordResponseDto> handleEmailDeliveryFailed() {
        return ResponseEntity.ok(
                ResetPasswordResponseDto.builder()
                        .success(false)
                        .message(EMAIL_FAILURE_MESSAGE)
                        .build()
        );
    }
}
//Lab6 : Wyjątki zamiast kodów błędów — przykład 1 (reset hasła) Stop
