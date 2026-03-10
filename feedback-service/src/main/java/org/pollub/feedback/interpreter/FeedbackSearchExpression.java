package org.pollub.feedback.interpreter;

import org.pollub.feedback.model.Feedback;
import java.util.List;

//start L3 Interpreter
public interface FeedbackSearchExpression {
    List<Feedback> interpret(List<Feedback> feedbacks);
}
//end L3 Interpreter
