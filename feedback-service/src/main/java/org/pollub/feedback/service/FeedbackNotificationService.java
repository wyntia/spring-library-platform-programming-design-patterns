package org.pollub.feedback.service;

import lombok.RequiredArgsConstructor;
import org.pollub.feedback.model.Feedback;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//Lab4 - SRP 3 Start
@RequiredArgsConstructor
//L4 - OCP 2 Start
public class FeedbackNotificationService {

    private final List<FeedbackNotificationHandler> notificationHandlers;

    public void sendSubmissionNotification(Feedback feedback) {
        for (FeedbackNotificationHandler notificationHandler : notificationHandlers) {
            notificationHandler.sendSubmissionNotification(feedback);
        }
    }
}
//L4 - OCP 2 END

//SRP3 End