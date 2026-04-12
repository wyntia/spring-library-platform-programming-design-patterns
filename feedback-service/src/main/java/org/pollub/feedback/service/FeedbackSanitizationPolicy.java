package org.pollub.feedback.service;

import org.pollub.feedback.model.Feedback;

//L4 - OCP 3 Start
public interface FeedbackSanitizationPolicy {

    void sanitize(Feedback feedback);
}
//L4 - OCP 3 END
