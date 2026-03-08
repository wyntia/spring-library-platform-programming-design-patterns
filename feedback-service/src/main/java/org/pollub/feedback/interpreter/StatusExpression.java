package org.pollub.feedback.interpreter;

import org.pollub.feedback.model.Feedback;
import org.pollub.feedback.model.FeedbackStatus;
import java.util.List;
import java.util.stream.Collectors;

//start L5 Interpreter
public class StatusExpression implements FeedbackSearchExpression {
    private final FeedbackStatus status;

    public StatusExpression(FeedbackStatus status) {
        this.status = status;
    }

    @Override
    public List<Feedback> interpret(List<Feedback> feedbacks) {
        if (status == null) return feedbacks;
        return feedbacks.stream()
                .filter(f -> f.getStatus() == status)
                .collect(Collectors.toList());
    }
}
//end L5 Interpreter
