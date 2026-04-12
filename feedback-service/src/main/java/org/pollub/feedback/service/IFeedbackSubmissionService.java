package org.pollub.feedback.service;

import org.pollub.feedback.model.Feedback;
import org.pollub.feedback.model.dto.FeedbackRequestDto;

//Lab5 : ISP 3 Start
public interface IFeedbackSubmissionService {
    Feedback submitFeedback(FeedbackRequestDto dto, String ipAddress);
}
//Lab5 : ISP 3 End
