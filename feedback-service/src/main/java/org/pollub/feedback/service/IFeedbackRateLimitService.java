package org.pollub.feedback.service;

//Lab5 : ISP 3 Start
public interface IFeedbackRateLimitService {
    boolean isRateLimitExceeded(String ipAddress);
    int[] getRateLimitInfo(String ipAddress);
}
//Lab5 : ISP 3 End
