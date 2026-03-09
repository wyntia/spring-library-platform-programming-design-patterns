package org.pollub.branch.strategy;

import lombok.RequiredArgsConstructor;
import org.pollub.branch.model.LibraryBranch;
import org.pollub.branch.repository.BranchRepository;
import org.springframework.stereotype.Component;

import java.util.List;

//L6 Strategy Design Pattern - City search strategy
/**
 * Strategy that searches branches only by city name.
 */
@Component
@RequiredArgsConstructor
public class CityBranchSearchStrategy implements BranchSearchStrategy {

    private final BranchRepository branchRepository;

    @Override
    public List<LibraryBranch> search(String query) {
        return branchRepository.findAll().stream()
                .filter(b -> b.getCity() != null &&
                        b.getCity().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }
}