package org.pollub.user.interpreter;

import org.pollub.user.model.User;
import java.util.List;
import java.util.stream.Collectors;

//start L3 Interpreter
public class UsernameExpression implements UserSearchExpression {
    private final String username;

    public UsernameExpression(String username) {
        this.username = username;
    }

    @Override
    public List<User> interpret(List<User> users) {
        if (username == null || username.isBlank()) return users;
        return users.stream()
                .filter(u -> u.getUsername() != null && u.getUsername().toLowerCase().contains(username.toLowerCase()))
                .collect(Collectors.toList());
    }
}
//end L3 Interpreter
