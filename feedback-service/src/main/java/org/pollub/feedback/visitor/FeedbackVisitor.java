package org.pollub.feedback.visitor;

import org.pollub.feedback.model.Feedback;

// L3 Visitor Design Pattern - Visitor interface for Feedback entities
/**
 * Visitor interface for operations on Feedback entities.
 */
public interface FeedbackVisitor {
    void visit(Feedback feedback);
}