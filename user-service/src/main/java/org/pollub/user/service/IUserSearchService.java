package org.pollub.user.service;

import org.pollub.user.model.User;

import java.util.List;
//Lab5 : ISP 1 Start

public interface IUserSearchService {
    List<User> findAll();

    List<User> searchUsers(String query);
}
//Lab5 : ISP 1 End