//start L2 Decorator
package org.pollub.branch.service;

import org.pollub.branch.model.LibraryBranch;
import org.pollub.branch.model.dto.BranchCreateDto;
import org.pollub.common.dto.UserDto;
import java.util.List;

/**
 * Interface for branch management operations.
 */
public interface IBranchService {
    List<LibraryBranch> getAllBranches();
    LibraryBranch getBranchById(Long id);
    LibraryBranch getBranchByNumber(String branchNumber);
    List<LibraryBranch> searchBranches(String query);
    LibraryBranch createBranch(BranchCreateDto dto);
    LibraryBranch updateBranch(Long id, BranchCreateDto dto);
    void deleteBranch(Long id);
    List<UserDto> getBranchEmployees(Long branchId);
    List<LibraryBranch> getBranchesByIds(List<Long> branchIds);
}
//end L2 Decorator
