package org.pollub.feedback.service;

import lombok.RequiredArgsConstructor;
import org.pollub.common.Observer;
import org.pollub.common.Subject;
import org.pollub.feedback.facade.FeedbackFacade;
import org.pollub.feedback.model.Feedback;
import org.pollub.feedback.model.FeedbackStatus;
import org.pollub.feedback.model.dto.FeedbackRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackService implements IFeedbackService, Subject {
    private final FeedbackFacade feedbackFacade;

    @Override
    public Feedback submitFeedback(FeedbackRequestDto dto, String ipAddress) {
        return feedbackFacade.submitFeedback(dto, ipAddress);
    }

    @Override
    public boolean isRateLimitExceeded(String ipAddress) {
        return feedbackFacade.isRateLimitExceeded(ipAddress);
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackFacade.getAllFeedbacks();
    }

    @Override
    public List<Feedback> getFeedbacksByStatus(FeedbackStatus status) {
        return feedbackFacade.getFeedbacksByStatus(status);
    }

    @Override
    public Feedback updateStatus(Long feedbackId, FeedbackStatus newStatus) {
        return feedbackFacade.updateStatus(feedbackId, newStatus);
    }

    @Override
    public int[] getRateLimitInfo(String ipAddress) {
        return feedbackFacade.getRateLimitInfo(ipAddress);
    }

    // Observer pattern implementation

    @Override
    public void attach(Observer observer) {
        feedbackFacade.attach(observer);
    }

    @Override
    public void detach(Observer observer) {
        feedbackFacade.detach(observer);
    }

    @Override
    public void notifyObservers(Object event) {
        feedbackFacade.notifyObservers(event);
    }

}
//SRP3 End