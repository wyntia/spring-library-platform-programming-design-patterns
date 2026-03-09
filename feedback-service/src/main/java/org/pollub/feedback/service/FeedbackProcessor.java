package org.pollub.feedback.service;

import org.pollub.common.exception.ResourceNotFoundException;
import org.pollub.feedback.model.Feedback;
import org.pollub.feedback.repository.IFeedbackRepository;

//L6 Template Method Design Pattern - Abstract base class for feedback processing
/**
 * Abstract base class for the Template Method pattern.
 * Defines the skeleton of a feedback processing operation.
 */
public abstract class FeedbackProcessor {
    protected final IFeedbackRepository feedbackRepository;

    protected FeedbackProcessor(IFeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    /**
     * The Template Method. Defines the fixed sequence of steps.
     */
    public final Feedback execute(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found: " + id));

        applyBusinessLogic(feedback);

        Feedback saved = feedbackRepository.save(feedback);

        onSuccess(saved);

        return saved;
    }

    protected abstract void applyBusinessLogic(Feedback feedback);

    protected void onSuccess(Feedback feedback) {
    }
}
