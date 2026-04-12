package org.pollub.feedback.service;

import org.pollub.feedback.model.Feedback;
import org.pollub.feedback.visitor.SecuritySanitizationVisitor;
import org.springframework.stereotype.Component;

@Component
//L4 - OCP 3 Start
public class VisitorFeedbackSanitizationPolicy implements FeedbackSanitizationPolicy {

    private final SecuritySanitizationVisitor securitySanitizationVisitor = new SecuritySanitizationVisitor();

    @Override
    public void sanitize(Feedback feedback) {
        // start L6 Visitor
        feedback.accept(securitySanitizationVisitor);
        // end L6 Visitor
    }
}
//L4 - OCP 3 END
