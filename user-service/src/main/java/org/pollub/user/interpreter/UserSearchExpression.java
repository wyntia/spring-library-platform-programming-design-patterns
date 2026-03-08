package org.pollub.user.interpreter;

import org.pollub.user.model.User;
import java.util.List;

//start L5 Interpreter
public interface UserSearchExpression {
    List<User> interpret(List<User> users);
}
//end L5 Interpreter
