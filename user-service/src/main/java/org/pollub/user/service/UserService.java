package org.pollub.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pollub.common.Observer;
import org.pollub.common.Subject;
import org.pollub.common.adapter.IPasswordGenerator;
import org.pollub.common.config.DateTimeProvider;
import org.pollub.common.dto.BranchDto;
import org.pollub.common.dto.UserAddressDto;
import org.pollub.common.event.UserEvent;
import org.pollub.common.exception.FavouriteLibraryNotSetException;
import org.pollub.common.exception.ResourceNotFoundException;
import org.pollub.common.exception.UserNotFoundException;
import org.pollub.user.client.IBranchServiceClient;
import org.pollub.user.dto.ApiTextResponse;
import org.pollub.user.dto.ChangePasswordDto;
import org.pollub.user.dto.ResetPasswordRequestDto;
import org.pollub.user.dto.ResetPasswordResponseDto;
import org.pollub.user.interpreter.AndUserExpression;
import org.pollub.user.interpreter.EmailExpression;
import org.pollub.user.interpreter.NameExpression;
import org.pollub.user.interpreter.SurnameExpression;
import org.pollub.user.interpreter.UserSearchExpression;
import org.pollub.user.interpreter.UsernameExpression;
import org.pollub.user.model.Role;
import org.pollub.user.model.User;
import org.pollub.user.model.UserAddress;
import org.pollub.user.repository.IUserRepository;
import org.pollub.user.service.utils.IUserFactory;
import org.pollub.user.service.utils.UserValidator;
import org.pollub.user.util.PasswordGenerator;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IUserService, Subject {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    //Lab1 - Factory Method Start
    private final IUserFactory userFactory;
    //Lab1 End Factory Method
    private final UserValidator userValidator;
    private final IBranchServiceClient branchServiceClient;
    private final IPasswordGenerator passwordGenerator;
    private final List<Observer> observers = new ArrayList<>();

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

    public List<User> searchUsers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }
        //start L5 Interpreter
        List<User> allUsers = userRepository.findAll();
        List<UserSearchExpression> expressions = List.of(
                new UsernameExpression(query),
                new EmailExpression(query),
                new NameExpression(query),
                new SurnameExpression(query)
        );
        UserSearchExpression andExpr = new AndUserExpression(expressions);
        List<User> filtered = andExpr.interpret(allUsers);
        //end L5 Interpreter
        return filtered;
    }

    @Transactional
    public User createUser(User user) {
        userValidator.validateNewUser(user);

        User createdUser = userFactory.createUser(
                user
        );
        User savedUser = userRepository.save(createdUser);

        // Notify observers about user creation
        notifyObservers(new UserEvent(
            "USER_CREATED",
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getEmail(),
            "New user account created",
            DateTimeProvider.getInstance().now()
        ));

        return savedUser;
    }

    @Transactional
    public User updateUser(Long id, User updatedUser) {
        User user = findById(id);
        user.setName(updatedUser.getName());
        user.setSurname(updatedUser.getSurname());
        user.setPhone(updatedUser.getPhone());
        User savedUser = userRepository.save(user);

        // Notify observers about user update
        notifyObservers(new UserEvent(
            "USER_UPDATED",
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getEmail(),
            "User profile updated",
            DateTimeProvider.getInstance().now()
        ));

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

        // Notify observers about address update
        notifyObservers(new UserEvent(
            "ADDRESS_UPDATED",
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getEmail(),
            "User address updated",
            DateTimeProvider.getInstance().now()
        ));

        return savedUser;
    }

    @Transactional
    public User updateRoles(Long id, Set<Role> roles) {
        User user = findById(id);
        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        // Notify observers about role change
        notifyObservers(new UserEvent(
            "ROLES_CHANGED",
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getEmail(),
            "User roles changed to: " + roles.stream().map(Role::toString).reduce((a, b) -> a + ", " + b).orElse("NONE"),
            DateTimeProvider.getInstance().now()
        ));

        return savedUser;
    }

    @Transactional
    public User updateFavouriteBranch(String username, Long branchId) {
        User user = findByUsername(username);
        user.setFavouriteBranchId(branchId);
        User savedUser = userRepository.save(user);

        // Notify observers about favourite branch change
        notifyObservers(new UserEvent(
            "FAVOURITE_BRANCH_UPDATED",
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getEmail(),
            "Favourite branch changed to: " + branchId,
            DateTimeProvider.getInstance().now()
        ));

        return savedUser;
    }

    @Transactional
    public User updateEmployeeBranch(Long userId, Long branchId) {
        User user = findById(userId);
        user.setEmployeeBranchId(branchId);
        User savedUser = userRepository.save(user);

        // Notify observers about employee branch change
        notifyObservers(new UserEvent(
            "EMPLOYEE_BRANCH_UPDATED",
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getEmail(),
            "Employee branch changed to: " + branchId,
            DateTimeProvider.getInstance().now()
        ));

        return savedUser;
    }

    public Long getFavouriteBranchId(Long userId) {
        User user = findById(userId);
        return user.getFavouriteBranchId();
    }

    public Long getEmployeeBranchId(Long userId) {
        User user = findById(userId);
        return user.getEmployeeBranchId();
    }

    public BranchDto getEmployeeBranch(String username) {
        User user = findByUsername(username);
        Long branchId = user.getEmployeeBranchId();
        
        if (branchId == null) {
            return null;
        }
        
        return branchServiceClient.getBranchById(branchId)
                .orElse(null);
    }
    
    /**
     * Get user's favourite branch with full details from branch-service.
     */
    public BranchDto getFavouriteBranch(Long userId) {
        User user = findById(userId);
        Long branchId = user.getFavouriteBranchId();

        if (branchId == null) {
            log.error("User with id {} does not have a favourite library set", userId);
            throw new FavouriteLibraryNotSetException(
                    userId
            );
        }
        
        return branchServiceClient.getBranchById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Biblioteka o id " + branchId + " nie została znaleziona"));
    }
    
    /**
     * Get user's employee branch with full details from branch-service.
     */
    public BranchDto getEmployeeBranchById(Long userId) {
        User user = findById(userId);
        Long branchId = user.getEmployeeBranchId();
        
        if (branchId == null) {
            throw new ResourceNotFoundException("Użytkownik nie jest przypisany do żadnej biblioteki jako pracownik");
        }
        
        return branchServiceClient.getBranchById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Biblioteka o id " + branchId + " nie została znaleziona"));
    }

    public List<User> findEmployeesByBranch(Long branchId) {
        return userRepository.findByEmployeeBranchId(branchId);
    }

    @Transactional
    public ApiTextResponse changePassword(String username, ChangePasswordDto passwordDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (passwordEncoder.matches(passwordDto.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Nowe hasło nie może być takie samo jak dotychczasowe.");
        }

        if (!passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Błąd przy zmianie hasła. Proszę zweryfikuj wpisane hasło.");
        }

        String encodedPassword = passwordEncoder.encode(passwordDto.getNewPassword());
        user.setPassword(encodedPassword);
        user.setMustChangePassword(false);  // User changed password after first login

        userRepository.save(user);

        // Notify observers about password change
        notifyObservers(new UserEvent(
            "PASSWORD_CHANGED",
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            "User password changed",
            DateTimeProvider.getInstance().now()
        ));

        return new ApiTextResponse(true, "Password for user " + username + " changed successfully");
    }

    /**
     * Validate user credentials for authentication
     */
    public User validateCredentials(String usernameOrEmail, String password) {
        User user = userRepository.findByEmail(usernameOrEmail)
                .or(() -> userRepository.findByUsername(usernameOrEmail.toLowerCase()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        
        return user;
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }

        User user = findById(id);
        userRepository.deleteById(id);

        // Notify observers about user deletion
        notifyObservers(new UserEvent(
            "USER_DELETED",
            id,
            user.getUsername(),
            user.getEmail(),
            "User account deleted",
            DateTimeProvider.getInstance().now()
        ));
    }

    /**
     * Reset password for a user identified by email and PESEL.
     * Generates a new temporary password, sets mustChangePassword to true, and returns the new password.
     */
    @Transactional
    public ResetPasswordResponseDto resetPassword(ResetPasswordRequestDto request) {
        // Find user by email and PESEL
        User user = userRepository.findByEmailAndPesel(request.getEmail(), request.getPesel())
                .orElse(null);
        
        // Always return success message for security (don't reveal if user exists)
        if (user == null) {
            return ResetPasswordResponseDto.builder()
                    .success(false)
                    .message("Jeśli podane dane są poprawne, nowe hasło zostanie wysłane na podany adres email.")
                    .build();
        }
        
        // Generate new temporary password
        String temporaryPassword = passwordGenerator.generate(); // Lab2 Adapter usage
        
        // Encode and save new password
        String encodedPassword = passwordEncoder.encode(temporaryPassword);
        user.setPassword(encodedPassword);
        user.setMustChangePassword(true);
        userRepository.save(user);
        
        // Notify observers about password reset
        notifyObservers(new UserEvent(
            "PASSWORD_RESET",
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            "User password reset with temporary password",
            DateTimeProvider.getInstance().now()
        ));

        return ResetPasswordResponseDto.builder()
                .email(user.getEmail())
                .temporaryPassword(temporaryPassword)
                .success(true)
                .message("Nowe hasło zostało wygenerowane i zostanie wysłane na podany adres email.")
                .build();
    }

    // Observer pattern implementation

    @Override
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            log.debug("Observer attached: {}", observer.getClass().getSimpleName());
        }
    }

    @Override
    public void detach(Observer observer) {
        if (observers.remove(observer)) {
            log.debug("Observer detached: {}", observer.getClass().getSimpleName());
        }
    }

    @Override
    public void notifyObservers(Object event) {
        for (Observer observer : observers) {
            observer.update(this, event);
        }
    }
}
