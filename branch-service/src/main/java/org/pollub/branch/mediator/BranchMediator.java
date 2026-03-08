package org.pollub.branch.mediator;

import org.pollub.branch.model.LibraryBranch;
import org.pollub.branch.model.dto.BranchCreateDto;
import org.pollub.common.dto.UserDto;
import java.util.List;

//start L5 Mediator
public interface BranchMediator {
    List<UserDto> getBranchEmployees(Long branchId);
    LibraryBranch createBranch(BranchCreateDto dto);
    LibraryBranch updateBranch(Long id, BranchCreateDto dto);
}
//end L5 Mediator
