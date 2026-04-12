package org.pollub.branch.service;

import org.pollub.branch.model.LibraryBranch;
import java.util.List;

//Lab5 : ISP 2 Start
public interface IBranchSearchService {
    List<LibraryBranch> searchBranches(String query);
}
//Lab5 : ISP 2 End
