package org.pollub.user.memento;

import org.pollub.user.model.User;

//start L5 Memento
public class UserMemento {
    private final User userSnapshot;

    public UserMemento(User user) {
        this.userSnapshot = new User();
        this.userSnapshot.setId(user.getId());
        this.userSnapshot.setName(user.getName());
        this.userSnapshot.setSurname(user.getSurname());
        this.userSnapshot.setReaderId(user.getReaderId());
        this.userSnapshot.setPhone(user.getPhone());
        this.userSnapshot.setUsername(user.getUsername());
        this.userSnapshot.setEmail(user.getEmail());
        this.userSnapshot.setPassword(user.getPassword());
        this.userSnapshot.setEnabled(user.isEnabled());
        this.userSnapshot.setRoles(user.getRoles());
        this.userSnapshot.setFavouriteBranchId(user.getFavouriteBranchId());
    }

    public User getSavedState() {
        return userSnapshot;
    }
}
//end L5 Memento
