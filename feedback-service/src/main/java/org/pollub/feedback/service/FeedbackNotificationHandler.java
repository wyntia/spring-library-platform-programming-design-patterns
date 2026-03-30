package org.pollub.feedback.service;

import org.pollub.feedback.model.Feedback;

//L4 - OCP 2 Start
public interface FeedbackNotificationHandler {

    void sendSubmissionNotification(Feedback feedback);
}
//L4 - OCP 2 END
