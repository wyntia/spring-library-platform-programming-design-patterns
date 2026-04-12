package org.pollub.branch.service;

import org.pollub.branch.model.LibraryBranch;
import java.util.List;

//Lab5 : ISP 2 Start
public interface IBranchQueryService {
    List<LibraryBranch> getAllBranches();
    LibraryBranch getBranchById(Long id);
    LibraryBranch getBranchByNumber(String branchNumber);
    List<LibraryBranch> getBranchesByIds(List<Long> branchIds);
}
//Lab5 : ISP 2 End
