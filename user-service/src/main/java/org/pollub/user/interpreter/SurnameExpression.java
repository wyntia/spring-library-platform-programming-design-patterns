package org.pollub.user.interpreter;

import org.pollub.user.model.User;
import java.util.List;
import java.util.stream.Collectors;

//start L3 Interpreter
public class SurnameExpression implements UserSearchExpression {
    private final String surname;

    public SurnameExpression(String surname) {
        this.surname = surname;
    }

    @Override
    public List<User> interpret(List<User> users) {
        if (surname == null || surname.isBlank()) return users;
        return users.stream()
                .filter(u -> u.getSurname() != null && u.getSurname().toLowerCase().contains(surname.toLowerCase()))
                .collect(Collectors.toList());
    }
}
//end L3 Interpreter
