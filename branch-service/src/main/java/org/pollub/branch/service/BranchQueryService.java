package org.pollub.branch.service;

import lombok.RequiredArgsConstructor;
import org.pollub.branch.model.LibraryBranch;
import org.pollub.branch.repository.BranchRepository;
import org.pollub.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//Lab4 - SRP 2 Start
public class BranchQueryService {

    private final BranchRepository branchRepository;

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

    public List<LibraryBranch> getBranchesByIds(List<Long> branchIds) {
        return branchRepository.findAllById(branchIds);
    }
}
//SRP2 End
