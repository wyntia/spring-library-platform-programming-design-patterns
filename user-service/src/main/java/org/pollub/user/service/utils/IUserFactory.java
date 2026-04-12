package org.pollub.user.service.utils;

import org.pollub.user.model.User;

//Lab1 - Factory 3 Method Start
public interface IUserFactory {
    //Lab6 : Znaczące nazewnictwo 2 Start
    User createUser(User user);
    //Lab6 : Znaczące nazewnictwo 2 Stop
    User createUser(String username, String email, String password, String name, String surname);
}
//Lab1 End Factory 3 Method
