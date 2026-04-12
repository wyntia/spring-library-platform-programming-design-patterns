package org.pollub.auth.exception;

//Lab6 : Wyjątki zamiast kodów błędów — przykład 1 (reset hasła) Start
/** User-service did not return a reset payload (e.g. transport error). */
public class PasswordResetUserServiceFailedException extends RuntimeException {

    public PasswordResetUserServiceFailedException() {
        super();
    }
}
//Lab6 : Wyjątki zamiast kodów błędów — przykład 1 (reset hasła) Stop
