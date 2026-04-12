package org.pollub.user.service;

import org.pollub.user.model.User;
//Lab5 : ISP 1 Start

public interface IUserQueryService {
    User findById(Long id);

    User findByUsername(String username);

    User findByEmail(String email);
}
//Lab5 : ISP 1 End