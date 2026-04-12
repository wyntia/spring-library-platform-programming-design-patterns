package org.pollub.user.service.search;

import org.pollub.user.interpreter.SurnameExpression;
import org.pollub.user.interpreter.UserSearchExpression;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
//L4 - OCP 1 Start
public class SurnameSearchExpressionFactory implements UserSearchExpressionFactory {

    @Override
    public UserSearchExpression create(String query) {
        return new SurnameExpression(query);
    }
}
//L4 - OCP 1 END
