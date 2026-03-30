package org.pollub.branch.service;

import lombok.RequiredArgsConstructor;
import org.pollub.branch.model.LibraryBranch;
import org.pollub.branch.strategy.DefaultBranchSearchStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//Lab4 - SRP 2 Start
public class BranchSearchService {

    private final DefaultBranchSearchStrategy searchStrategy;
    private final BranchQueryService branchQueryService;

    public List<LibraryBranch> searchBranches(String query) {
        if (query == null || query.trim().isEmpty()) {
            return branchQueryService.getAllBranches();
        }
        return searchStrategy.search(query);
    }
}
//SRP2 End
