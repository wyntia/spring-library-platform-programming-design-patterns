package org.pollub.branch.service;

import org.pollub.branch.model.LibraryBranch;
import org.pollub.branch.model.dto.BranchCreateDto;

//Lab5 : ISP 2 Start
public interface IBranchCommandService {
    LibraryBranch createBranch(BranchCreateDto dto);
    LibraryBranch updateBranch(Long id, BranchCreateDto dto);
    void deleteBranch(Long id);
}
//Lab5 : ISP 2 End
