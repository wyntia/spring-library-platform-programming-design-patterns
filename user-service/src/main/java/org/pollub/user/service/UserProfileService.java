package org.pollub.user.service;

import lombok.RequiredArgsConstructor;
import org.pollub.common.dto.UserAddressDto;
import org.pollub.common.exception.ResourceNotFoundException;
import org.pollub.user.interpreter.AndUserExpression;
import org.pollub.user.interpreter.UserSearchExpression;
import org.pollub.user.model.Role;
import org.pollub.user.model.User;
import org.pollub.user.model.UserAddress;
import org.pollub.user.repository.IUserRepository;
import org.pollub.user.service.search.UserSearchExpressionFactory;
import org.pollub.user.service.utils.IUserFactory;
import org.pollub.user.service.utils.UserValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
// Lab4 - SRP 1 Start
// Lab5 : ISP 1 Start
public class UserProfileService implements IUserSearchService, IUserQueryService, IUserCommandService {
    // Lab5 : ISP 1 End
    private final IUserRepository userRepository;
    private final IUserFactory userFactory;
    private final UserValidator userValidator;
    private final UserEventPublisher userEventPublisher;
    // L4 - OCP 1 Start
    private final List<UserSearchExpressionFactory> userSearchExpressionFactories;
    // L4 - OCP 1 END
    // Lab5 : DIP 1 Start
    private final org.pollub.user.service.utils.logger.IUserActivityLogger activityLogger;
    // Lab5 : DIP 1 End
    // Lab5 : DIP 2 Start
    private final org.pollub.user.service.utils.exporter.IProfileExporter profileExporter;
    // Lab5 : DIP 2 End

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    //Lab6 : Poziom abstrakcji 3 Start
    public List<User> searchUsers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }

        List<User> candidates = loadAllUsersForSearch();
        UserSearchExpression expression = buildCombinedSearchExpression(query);
        return executeUserSearch(candidates, expression);
    }

    private List<User> loadAllUsersForSearch() {
        return userRepository.findAll();
    }

    private UserSearchExpression buildCombinedSearchExpression(String query) {
        // L4 - OCP 1 Start
        List<UserSearchExpression> expressions = userSearchExpressionFactories.stream()
                .map(factory -> factory.create(query))
                .toList();
        // L4 - OCP 1 END
        return new AndUserExpression(expressions);
    }

    private static List<User> executeUserSearch(List<User> candidates, UserSearchExpression expression) {
        return expression.interpret(candidates);
    }
    //Lab6 : Poziom abstrakcji 3 Stop

    @Transactional
    public User createUser(User user) {
        userValidator.validateNewUser(user);

        User createdUser = userFactory.createUser(user);
        User savedUser = userRepository.save(createdUser);

        userEventPublisher.publish("USER_CREATED", savedUser, "New user account created");

        return savedUser;
    }

    @Transactional
    public User updateUser(Long id, User updatedUser) {
        User user = findById(id);
        user.setName(updatedUser.getName());
        user.setSurname(updatedUser.getSurname());
        user.setPhone(updatedUser.getPhone());

        User savedUser = userRepository.save(user);
        userEventPublisher.publish("USER_UPDATED", savedUser, "User profile updated");

        // Lab5 : DIP 1 Start
        activityLogger.logActivity(id, "Zaktualizowano profil");
        // Lab5 : DIP 1 End

        return savedUser;
    }

    @Transactional
    public User updateAddress(Long id, UserAddressDto addressDto) {
        User user = findById(id);
        UserAddress address = UserAddress.builder()
                .street(addressDto.getStreet())
                .city(addressDto.getCity())
                .postalCode(addressDto.getPostalCode())
                .country(addressDto.getCountry())
                .buildingNumber(addressDto.getBuildingNumber())
                .apartmentNumber(addressDto.getApartmentNumber())
                .build();
        user.setAddress(address);

        User savedUser = userRepository.save(user);
        userEventPublisher.publish("ADDRESS_UPDATED", savedUser, "User address updated");

        return savedUser;
    }

    @Transactional
    public User updateRoles(Long id, Set<Role> roles) {
        User user = findById(id);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        userEventPublisher.publish(
                "ROLES_CHANGED",
                savedUser,
                "User roles changed to: "
                        + roles.stream().map(Role::toString).reduce((a, b) -> a + ", " + b).orElse("NONE"));

        return savedUser;
    }

    public List<User> findEmployeesByBranch(Long branchId) {
        return userRepository.findByEmployeeBranchId(branchId);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }

        User user = findById(id);
        userRepository.deleteById(id);

        userEventPublisher.publish("USER_DELETED", id, user.getUsername(), user.getEmail(), "User account deleted");
    }

    // Lab5 : DIP 2 Start
    public byte[] exportUserProfile(Long id) {
        return profileExporter.exportProfile(id);
    }
    // Lab5 : DIP 2 End
}

// SRP1 End