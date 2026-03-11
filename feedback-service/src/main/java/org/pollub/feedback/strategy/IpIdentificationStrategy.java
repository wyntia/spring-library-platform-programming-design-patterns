package org.pollub.feedback.strategy;

import jakarta.servlet.http.HttpServletRequest;

// L3 Strategy Design Pattern - Strategy interface for IP identification
public interface IpIdentificationStrategy {
    String identify(HttpServletRequest request);
}
