package org.pollub.user.interpreter;

import org.pollub.user.model.User;
import java.util.List;
import java.util.stream.Collectors;

//start L5 Interpreter
public class EmailExpression implements UserSearchExpression {
    private final String email;

    public EmailExpression(String email) {
        this.email = email;
    }

    @Override
    public List<User> interpret(List<User> users) {
        if (email == null || email.isBlank()) return users;
        return users.stream()
                .filter(u -> u.getEmail() != null && u.getEmail().toLowerCase().contains(email.toLowerCase()))
                .collect(Collectors.toList());
    }
}
//end L5 Interpreter
