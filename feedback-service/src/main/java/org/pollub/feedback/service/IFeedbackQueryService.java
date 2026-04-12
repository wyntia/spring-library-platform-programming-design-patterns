package org.pollub.feedback.service;
import org.pollub.feedback.model.Feedback;
import org.pollub.feedback.model.FeedbackStatus;

import java.util.List;

//Lab5 : ISP 3 Start
public interface IFeedbackQueryService {
    List<Feedback> getAllFeedbacks();
    List<Feedback> getFeedbacksByStatus(FeedbackStatus status);
}
//Lab5 : ISP 3 End