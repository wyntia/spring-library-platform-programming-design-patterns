package org.pollub.feedback.service;
import org.pollub.feedback.model.Feedback;
import org.pollub.feedback.model.FeedbackStatus;

//Lab5 : ISP 3 Start
public interface IFeedbackStatusModerationService {
    Feedback updateStatus(Long feedbackId, FeedbackStatus newStatus);
}
//Lab5 : ISP 3 End
