package org.pollub.user.mediator;

import lombok.RequiredArgsConstructor;
import org.pollub.user.model.User;
import org.pollub.user.service.utils.IUserFactory;
import org.pollub.user.service.utils.UserValidator;
import org.pollub.user.repository.IUserRepository;
import org.pollub.user.client.IBranchServiceClient;
import org.pollub.common.adapter.IPasswordGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.pollub.user.dto.UserRegistrationDto;
import org.pollub.common.dto.BranchDto;
import org.pollub.common.exception.ResourceNotFoundException;

//start L5 Mediator

@Component
@RequiredArgsConstructor
public class UserRegistrationMediator {
    private final IUserFactory userFactory;
    private final UserValidator userValidator;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IPasswordGenerator passwordGenerator;
    private final IBranchServiceClient branchServiceClient;

    //start L5 Mediator
    public User registerUser(UserRegistrationDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setPhone(dto.getPhone());

        userValidator.validateNewUser(user);

        //Lab 1 - Factory
        user = userFactory.createUser(user);

        String rawPassword = passwordGenerator.generate();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        user.setMustChangePassword(true);

        if (dto.getFavouriteBranchId() != null) {
            BranchDto branch = branchServiceClient.getBranchById(dto.getFavouriteBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch", dto.getFavouriteBranchId()));
            user.setFavouriteBranchId(branch.getId());
        }

        return userRepository.save(user);
    }
    //end L5 Mediator
}
