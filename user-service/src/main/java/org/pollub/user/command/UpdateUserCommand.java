package org.pollub.user.command;

import org.pollub.user.model.User;
import org.pollub.user.memento.UserMemento;
import org.pollub.user.service.UserService;

//start L3 Command
public class UpdateUserCommand {
    private final UserService userService;
    private final Long id;
    private final User newUserData;
    private UserMemento memento; //obiekt Memento

    public UpdateUserCommand(UserService userService, Long id, User newUserData) {
        this.userService = userService;
        this.id = id;
        this.newUserData = newUserData;
    }

    public User execute() {
        User before = userService.findById(id);
        //start L3 Memento
        this.memento = new UserMemento(before);
        //end L3 Memento
        return userService.updateUser(id, newUserData);
    }

    //start L3 Memento
    public UserMemento getMemento() {
        return memento;
    }
    //end L3 Memento
}
//end L3 Command
