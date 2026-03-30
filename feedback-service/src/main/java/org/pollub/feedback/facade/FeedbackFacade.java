package org.pollub.feedback.facade;

import lombok.RequiredArgsConstructor;
import org.pollub.common.Observer;
import org.pollub.common.Subject;
import org.pollub.feedback.model.Feedback;
import org.pollub.feedback.model.FeedbackStatus;
import org.pollub.feedback.model.dto.FeedbackRequestDto;
import org.pollub.feedback.service.FeedbackEventPublisher;
import org.pollub.feedback.service.FeedbackModerationService;
import org.pollub.feedback.service.FeedbackRateLimitService;
import org.pollub.feedback.service.FeedbackSubmissionService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
//Lab4 - SRP 3 Start
public class FeedbackFacade implements Subject {

    private final FeedbackSubmissionService feedbackSubmissionService;
    private final FeedbackRateLimitService feedbackRateLimitService;
    private final FeedbackModerationService feedbackModerationService;
    private final FeedbackEventPublisher feedbackEventPublisher;

    public Feedback submitFeedback(FeedbackRequestDto dto, String ipAddress) {
        return feedbackSubmissionService.submitFeedback(dto, ipAddress);
    }

    public boolean isRateLimitExceeded(String ipAddress) {
        return feedbackRateLimitService.isRateLimitExceeded(ipAddress);
    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackModerationService.getAllFeedbacks();
    }

    public List<Feedback> getFeedbacksByStatus(FeedbackStatus status) {
        return feedbackModerationService.getFeedbacksByStatus(status);
    }

    public Feedback updateStatus(Long feedbackId, FeedbackStatus newStatus) {
        return feedbackModerationService.updateStatus(feedbackId, newStatus);
    }

    public int[] getRateLimitInfo(String ipAddress) {
        return feedbackRateLimitService.getRateLimitInfo(ipAddress);
    }

    @Override
    public void attach(Observer observer) {
        feedbackEventPublisher.attach(observer);
    }

    @Override
    public void detach(Observer observer) {
        feedbackEventPublisher.detach(observer);
    }

    @Override
    public void notifyObservers(Object event) {
        feedbackEventPublisher.notifyObservers(event);
    }
}
//SRP3 End
