package org.pollub.user.facade;

import lombok.RequiredArgsConstructor;
import org.pollub.common.Observer;
import org.pollub.common.Subject;
import org.pollub.common.dto.BranchDto;
import org.pollub.common.dto.UserAddressDto;
import org.pollub.user.dto.ApiTextResponse;
import org.pollub.user.dto.ChangePasswordDto;
import org.pollub.user.dto.ResetPasswordRequestDto;
import org.pollub.user.dto.ResetPasswordResponseDto;
import org.pollub.user.model.Role;
import org.pollub.user.model.User;
import org.pollub.user.service.UserBranchService;
import org.pollub.user.service.UserEventPublisher;
import org.pollub.user.service.UserProfileService;
import org.pollub.user.service.UserSecurityService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
//Lab4 - SRP 1 Start
public class UserFacade implements Subject {

    private final UserProfileService userProfileService;
    private final UserSecurityService userSecurityService;
    private final UserBranchService userBranchService;
    private final UserEventPublisher userEventPublisher;

    public User findById(Long id) {
        return userProfileService.findById(id);
    }

    public User findByUsername(String username) {
        return userProfileService.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userProfileService.findByEmail(email);
    }

    public List<User> findAll() {
        return userProfileService.findAll();
    }

    public List<User> searchUsers(String query) {
        return userProfileService.searchUsers(query);
    }

    public User createUser(User user) {
        return userProfileService.createUser(user);
    }

    public User updateUser(Long id, User updatedUser) {
        return userProfileService.updateUser(id, updatedUser);
    }

    public User updateAddress(Long id, UserAddressDto addressDto) {
        return userProfileService.updateAddress(id, addressDto);
    }

    public User updateRoles(Long id, Set<Role> roles) {
        return userProfileService.updateRoles(id, roles);
    }

    public User updateFavouriteBranch(String username, Long branchId) {
        return userBranchService.updateFavouriteBranch(username, branchId);
    }

    public User updateEmployeeBranch(Long userId, Long branchId) {
        return userBranchService.updateEmployeeBranch(userId, branchId);
    }

    public Long getFavouriteBranchId(Long userId) {
        return userBranchService.getFavouriteBranchId(userId);
    }

    public Long getEmployeeBranchId(Long userId) {
        return userBranchService.getEmployeeBranchId(userId);
    }

    public BranchDto getEmployeeBranch(String username) {
        return userBranchService.getEmployeeBranch(username);
    }

    public BranchDto getFavouriteBranch(Long userId) {
        return userBranchService.getFavouriteBranch(userId);
    }

    public BranchDto getEmployeeBranchById(Long userId) {
        return userBranchService.getEmployeeBranchById(userId);
    }

    public List<User> findEmployeesByBranch(Long branchId) {
        return userProfileService.findEmployeesByBranch(branchId);
    }

    public ApiTextResponse changePassword(String username, ChangePasswordDto passwordDto) {
        return userSecurityService.changePassword(username, passwordDto);
    }

    public User validateCredentials(String usernameOrEmail, String password) {
        return userSecurityService.validateCredentials(usernameOrEmail, password);
    }

    public ResetPasswordResponseDto resetPassword(ResetPasswordRequestDto request) {
        return userSecurityService.resetPassword(request);
    }

    public void deleteUser(Long id) {
        userProfileService.deleteUser(id);
    }

    @Override
    public void attach(Observer observer) {
        userEventPublisher.attach(observer);
    }

    @Override
    public void detach(Observer observer) {
        userEventPublisher.detach(observer);
    }

    @Override
    public void notifyObservers(Object event) {
        userEventPublisher.notifyObservers(event);
    }
}
//SRP1 End