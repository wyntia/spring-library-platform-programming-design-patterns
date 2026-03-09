package org.pollub.branch.service;

import lombok.RequiredArgsConstructor;
import org.pollub.branch.client.UserServiceClient;
import org.pollub.branch.mediator.EmployeeTransferMediator;
import org.pollub.branch.model.LibraryBranch;
import org.pollub.branch.model.dto.BranchCreateDto;
import org.pollub.branch.repository.BranchRepository;
import org.pollub.common.dto.UserDto;
import org.pollub.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("baseBranchService")
@Transactional
@RequiredArgsConstructor
public class BranchService implements IBranchService {
    private final BranchRepository branchRepository;
    private final UserServiceClient userServiceClient;
    private final EmployeeTransferMediator employeeTransferMediator;

    public boolean transferEmployeeToBranch(Long employeeId, Long targetBranchId) {
        //start L5 Mediator
        return employeeTransferMediator.transferEmployee(employeeId, targetBranchId);
        //end L5 Mediator
    }
    
    public List<LibraryBranch> getAllBranches() {
        return branchRepository.findAll();
    }
    
    public LibraryBranch getBranchById(Long id) {
        return branchRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("LibraryBranch", id));
    }
    
    public LibraryBranch getBranchByNumber(String branchNumber) {
        return branchRepository.findByBranchNumber(branchNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Branch not found with number: " + branchNumber));
    }
    
    public List<LibraryBranch> searchBranches(String query) {
        if (query == null || query.trim().isEmpty()) {
            return branchRepository.findAll();
        }
        return branchRepository.searchBranches(query.trim());
    }
    
    public LibraryBranch createBranch(BranchCreateDto dto) {
        LibraryBranch branch = new LibraryBranch();
        branch.setBranchNumber(dto.getBranchNumber());
        branch.setName(dto.getName());
        branch.setAddress(dto.getAddress());
        return branchRepository.save(branch);
    }
    
    public LibraryBranch updateBranch(Long id, BranchCreateDto dto) {
        LibraryBranch branch = branchRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("LibraryBranch", id));
        branch.setBranchNumber(dto.getBranchNumber());
        branch.setName(dto.getName());
        branch.setAddress(dto.getAddress());
        return branchRepository.save(branch);
    }
    
    public void deleteBranch(Long id) {
        if (!branchRepository.existsById(id)) {
            throw new ResourceNotFoundException("LibraryBranch", id);
        }
        branchRepository.deleteById(id);
    }
    
    /**
     * Get employees assigned to this branch from user-service
     */
    public List<UserDto> getBranchEmployees(Long branchId) {
        return userServiceClient.getEmployeesByBranch(branchId);
    }

    /**
     * Get multiple branches by IDs
     */
    public List<LibraryBranch> getBranchesByIds(List<Long> branchIds) {
        return branchRepository.findAllById(branchIds);
    }
}
