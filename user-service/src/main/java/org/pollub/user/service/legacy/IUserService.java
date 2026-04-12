package org.pollub.user.service.legacy;

import org.pollub.common.dto.BranchDto;
import org.pollub.common.dto.UserAddressDto;
import org.pollub.user.dto.ApiTextResponse;
import org.pollub.user.dto.ChangePasswordDto;
import org.pollub.user.dto.ResetPasswordRequestDto;
import org.pollub.user.dto.ResetPasswordResponseDto;
import org.pollub.user.model.Role;
import org.pollub.user.model.User;

import java.util.List;
import java.util.Set;

//Lab5 : ISP 1 Start
/**
 * Gruby interfejs (Fat Interface) sprzed refaktoryzacji SRP.
 * Wcześniej wszystkie te metody były wrzucone do jednego worka UserService.
 */
public interface IUserService {
    // UserProfileService methods
    User findById(Long id);
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findAll();
    List<User> searchUsers(String query);
    User createUser(User user);
    User updateUser(Long id, User updatedUser);
    User updateAddress(Long id, UserAddressDto addressDto);
    User updateRoles(Long id, Set<Role> roles);
    List<User> findEmployeesByBranch(Long branchId);
    void deleteUser(Long id);
    byte[] exportUserProfile(Long id);

    // UserSecurityService methods
    ApiTextResponse changePassword(String username, ChangePasswordDto passwordDto);
    User validateCredentials(String usernameOrEmail, String password);
    ResetPasswordResponseDto resetPassword(ResetPasswordRequestDto request);

    // UserBranchService methods
    User updateFavouriteBranch(String username, Long branchId);
    User updateEmployeeBranch(Long userId, Long branchId);
    Long getFavouriteBranchId(Long userId);
    Long getEmployeeBranchId(Long userId);
    BranchDto getEmployeeBranch(String username);
    BranchDto getFavouriteBranch(Long userId);
    BranchDto getEmployeeBranchById(Long userId);
}
//Lab5 : ISP 1 End
