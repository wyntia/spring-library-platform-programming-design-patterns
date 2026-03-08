package org.pollub.user.interpreter;

import org.pollub.user.model.User;
import java.util.List;

//start L5 Interpreter
public class AndUserExpression implements UserSearchExpression {
    private final List<UserSearchExpression> expressions;

    public AndUserExpression(List<UserSearchExpression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public List<User> interpret(List<User> users) {
        List<User> result = users;
        for (UserSearchExpression expr : expressions) {
            result = expr.interpret(result);
        }
        return result;
    }
}
//end L5 Interpreter
