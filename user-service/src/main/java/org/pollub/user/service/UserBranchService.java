package org.pollub.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pollub.common.dto.BranchDto;
import org.pollub.common.exception.FavouriteLibraryNotSetException;
import org.pollub.common.exception.ResourceNotFoundException;
import org.pollub.user.client.IBranchServiceClient;
import org.pollub.user.model.User;
import org.pollub.user.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
//Lab4 - SRP 1 Start
public class UserBranchService implements IEmployeeBranchAssignmentService, IUserFavouriteBranchService {
    private final IUserRepository userRepository;
    private final IBranchServiceClient branchServiceClient;
    private final UserEventPublisher userEventPublisher;

    @Transactional
    public User updateFavouriteBranch(String username, Long branchId) {
        User user = findByUsername(username);
        user.setFavouriteBranchId(branchId);

        User savedUser = userRepository.save(user);
        userEventPublisher.publish("FAVOURITE_BRANCH_UPDATED", savedUser, "Favourite branch changed to: " + branchId);

        return savedUser;
    }

    @Transactional
    public User updateEmployeeBranch(Long userId, Long branchId) {
        User user = findById(userId);
        user.setEmployeeBranchId(branchId);

        User savedUser = userRepository.save(user);
        userEventPublisher.publish("EMPLOYEE_BRANCH_UPDATED", savedUser, "Employee branch changed to: " + branchId);

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

    public BranchDto getFavouriteBranch(Long userId) {
        User user = findById(userId);
        Long branchId = user.getFavouriteBranchId();

        if (branchId == null) {
            log.error("User with id {} does not have a favourite library set", userId);
            throw new FavouriteLibraryNotSetException(userId);
        }

        return branchServiceClient.getBranchById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Biblioteka o id " + branchId + " nie została znaleziona"));
    }

    public BranchDto getEmployeeBranchById(Long userId) {
        User user = findById(userId);
        Long branchId = user.getEmployeeBranchId();

        if (branchId == null) {
            throw new ResourceNotFoundException("Użytkownik nie jest przypisany do żadnej biblioteki jako pracownik");
        }

        return branchServiceClient.getBranchById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Biblioteka o id " + branchId + " nie została znaleziona"));
    }

    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    private User findByUsername(String username) {
        return userRepository.findByUsername(username.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
}
//SRP1 End
