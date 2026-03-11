package org.pollub.branch.strategy;

import lombok.RequiredArgsConstructor;
import org.pollub.branch.model.LibraryBranch;
import org.pollub.branch.repository.BranchRepository;
import org.springframework.stereotype.Component;

import java.util.List;

//L3 Strategy Design Pattern - Default search strategy using JPQL query across all fields
/**
 * Default strategy - searches branches by name, city, address or branch number.
 * Preserves the existing repository JPQL query logic.
 */
@Component
@RequiredArgsConstructor
public class DefaultBranchSearchStrategy implements BranchSearchStrategy {

    private final BranchRepository branchRepository;

    @Override
    public List<LibraryBranch> search(String query) {
        return branchRepository.searchBranches(query);
    }
}