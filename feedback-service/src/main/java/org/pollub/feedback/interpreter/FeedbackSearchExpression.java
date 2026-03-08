package org.pollub.feedback.interpreter;

import org.pollub.feedback.model.Feedback;
import java.util.List;

//start L5 Interpreter
public interface FeedbackSearchExpression {
    List<Feedback> interpret(List<Feedback> feedbacks);
}
//end L5 Interpreter
