package org.pollub.feedback.memento;

import org.pollub.feedback.model.Feedback;

//start L5 Memento
public class FeedbackMemento {
    private final Feedback feedbackSnapshot;

    public FeedbackMemento(Feedback feedback) {
        this.feedbackSnapshot = new Feedback();
        this.feedbackSnapshot.setId(feedback.getId());
        this.feedbackSnapshot.setStatus(feedback.getStatus());
        this.feedbackSnapshot.setMessage(feedback.getMessage());
        this.feedbackSnapshot.setCreatedAt(feedback.getCreatedAt());
        this.feedbackSnapshot.setIpAddress(feedback.getIpAddress());
        this.feedbackSnapshot.setCategory(feedback.getCategory());
        this.feedbackSnapshot.setPageUrl(feedback.getPageUrl());
        this.feedbackSnapshot.setResolvedAt(feedback.getResolvedAt());
    }

    public Feedback getSavedState() {
        return feedbackSnapshot;
    }
}
//end L5 Memento
