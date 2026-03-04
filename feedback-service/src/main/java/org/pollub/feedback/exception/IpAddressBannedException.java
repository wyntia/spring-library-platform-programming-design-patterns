package org.pollub.feedback.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class IpAddressBannedException extends RuntimeException {
    public IpAddressBannedException(String ipAddress) {
        super("Access denied: IP address " + ipAddress + " is banned.");
    }
}
