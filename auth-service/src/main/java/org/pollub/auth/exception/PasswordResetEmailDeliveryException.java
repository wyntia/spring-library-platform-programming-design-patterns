package org.pollub.auth.exception;

//Lab6 : Wyjątki zamiast kodów błędów — przykład 1 (reset hasła) Start
/** Failed to send the password reset email after user-service succeeded. */
public class PasswordResetEmailDeliveryException extends RuntimeException {

    public PasswordResetEmailDeliveryException(Throwable cause) {
        super(cause);
    }
}
//Lab6 : Wyjątki zamiast kodów błędów — przykład 1 (reset hasła) Stop
