package org.pollub.user.service;

import org.pollub.common.dto.UserAddressDto;
import org.pollub.user.model.Role;
import org.pollub.user.model.User;

import java.util.Set;

//Lab5 : ISP 1 Start

public interface IUserCommandService {
    User createUser(User user);

    User updateUser(Long id, User updatedUser);

    void deleteUser(Long id);

    User updateRoles(Long id, Set<Role> roles);

    User updateAddress(Long id, UserAddressDto addressDto);
}

//Lab5 : ISP 1 End