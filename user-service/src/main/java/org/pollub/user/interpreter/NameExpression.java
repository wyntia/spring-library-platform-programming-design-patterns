package org.pollub.user.interpreter;

import org.pollub.user.model.User;
import java.util.List;
import java.util.stream.Collectors;

//start L3 Interpreter
public class NameExpression implements UserSearchExpression {
    private final String name;

    public NameExpression(String name) {
        this.name = name;
    }

    @Override
    public List<User> interpret(List<User> users) {
        if (name == null || name.isBlank()) return users;
        return users.stream()
                .filter(u -> u.getName() != null && u.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
}
//end L3 Interpreter
