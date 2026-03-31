package org.pollub.user.service.search;

import org.pollub.user.interpreter.UserSearchExpression;
import org.pollub.user.interpreter.UsernameExpression;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
//L4 - OCP 1 Start
public class UsernameSearchExpressionFactory implements UserSearchExpressionFactory {

    @Override
    public UserSearchExpression create(String query) {
        return new UsernameExpression(query);
    }
}
//L4 - OCP 1 END
