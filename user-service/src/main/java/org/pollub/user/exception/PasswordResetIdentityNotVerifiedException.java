package org.pollub.user.exception;

//Lab6 : Wyjątki zamiast kodów błędów — przykład 1 (reset hasła) Start
/**
 * Thrown when email + PESEL do not match a user during password reset.
 * Mapped to the same generic HTTP 200 response as before (anti-enumeration).
 */
public class PasswordResetIdentityNotVerifiedException extends RuntimeException {

    public PasswordResetIdentityNotVerifiedException() {
        super();
    }
}
//Lab6 : Wyjątki zamiast kodów błędów — przykład 1 (reset hasła) Stop
