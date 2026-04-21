package org.pollub.user.service.search;

import org.pollub.user.interpreter.EmailExpression;
import org.pollub.user.interpreter.UserSearchExpression;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
//L4 - OCP 1 Start
public class EmailSearchExpressionFactory implements UserSearchExpressionFactory {

    @Override
    public UserSearchExpression create(String query) {
        return new EmailExpression(query);
    }
}
//L4 - OCP 1 END
