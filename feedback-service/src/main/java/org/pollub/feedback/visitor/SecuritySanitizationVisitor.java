package org.pollub.feedback.visitor;

import org.pollub.feedback.model.Feedback;

//L6 Visitor Design Pattern - Concrete Visitor for security sanitization
/**
 * Concrete Visitor responsible for cleaning feedback data from XSS and other threats.
 */
public class SecuritySanitizationVisitor implements FeedbackVisitor {

    @Override
    public void visit(Feedback feedback) {
        String message = feedback.getMessage();
        if (message != null) {
            // Logic moved from FeedbackService to separate sanitization concerns
            String sanitized = message
                    .replaceAll("<[^>]*>", "") // Remove all HTML tags
                    .replaceAll("(?i)javascript:", "") // Remove javascript: URLs
                    .replaceAll("(?i)data:", "") // Remove data: URLs
                    .replaceAll("(?i)vbscript:", "") // Remove vbscript: URLs
                    .trim();
            feedback.setMessage(sanitized);
        }
    }
}