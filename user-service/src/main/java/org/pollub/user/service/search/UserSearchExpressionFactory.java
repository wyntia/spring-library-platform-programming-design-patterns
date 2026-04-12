package org.pollub.user.service.search;

import org.pollub.user.interpreter.UserSearchExpression;

//L4 - OCP 1 Start
public interface UserSearchExpressionFactory {

    UserSearchExpression create(String query);
}
//L4 - OCP 1 END
