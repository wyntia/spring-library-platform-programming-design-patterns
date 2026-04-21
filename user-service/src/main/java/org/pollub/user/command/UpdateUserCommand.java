package org.pollub.user.command;

import org.pollub.user.model.User;
import org.pollub.user.memento.UserMemento;
import org.pollub.user.facade.UserFacade;

//start L3 Command
public class UpdateUserCommand {
    private final UserFacade userFacade;
    private final Long id;
    private final User newUserData;
    private UserMemento memento; //obiekt Memento

    public UpdateUserCommand(UserFacade userFacade, Long id, User newUserData) {
        this.userFacade = userFacade;
        this.id = id;
        this.newUserData = newUserData;
    }

    public User execute() {
        User before = userFacade.findById(id);
        //start L3 Memento
        this.memento = new UserMemento(before);
        //end L3 Memento
        return userFacade.updateUser(id, newUserData);
    }

    //start L3 Memento
    public UserMemento getMemento() {
        return memento;
    }
    //end L3 Memento
}
//end L3 Command
