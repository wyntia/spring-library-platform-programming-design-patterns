package org.pollub.branch.service;

import lombok.RequiredArgsConstructor;
import org.pollub.branch.facade.BranchFacade;
import org.pollub.branch.model.LibraryBranch;
import org.pollub.branch.model.dto.BranchCreateDto;
import org.pollub.common.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("baseBranchService")
@Transactional
@RequiredArgsConstructor

public class BranchService implements IBranchService {

    private final BranchFacade branchFacade;

    public List<LibraryBranch> getAllBranches() {
        return branchFacade.getAllBranches();
    }
    
    public LibraryBranch getBranchById(Long id) {
        return branchFacade.getBranchById(id);
    }
    
    public LibraryBranch getBranchByNumber(String branchNumber) {
        return branchFacade.getBranchByNumber(branchNumber);
    }
    
    public List<LibraryBranch> searchBranches(String query) {
        return branchFacade.searchBranches(query);
    }

    public LibraryBranch createBranch(BranchCreateDto dto) {
        return branchFacade.createBranch(dto);
    }

    public LibraryBranch updateBranch(Long id, BranchCreateDto dto) {
        return branchFacade.updateBranch(id, dto);
    }
    
    public void deleteBranch(Long id) {
        branchFacade.deleteBranch(id);
    }
    
    /**
     * Get employees assigned to this branch from user-service
     */
    public List<UserDto> getBranchEmployees(Long branchId) {
        return branchFacade.getBranchEmployees(branchId);
    }

    /**
     * Get multiple branches by IDs
     */
    public List<LibraryBranch> getBranchesByIds(List<Long> branchIds) {
        return branchFacade.getBranchesByIds(branchIds);
    }
}
