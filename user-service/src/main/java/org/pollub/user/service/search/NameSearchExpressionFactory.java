package org.pollub.user.service.search;

import org.pollub.user.interpreter.NameExpression;
import org.pollub.user.interpreter.UserSearchExpression;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
//L4 - OCP 1 Start
public class NameSearchExpressionFactory implements UserSearchExpressionFactory {

    @Override
    public UserSearchExpression create(String query) {
        return new NameExpression(query);
    }
}
//L4 - OCP 1 END
