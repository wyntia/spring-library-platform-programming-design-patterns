package org.pollub.branch.strategy;

import org.pollub.branch.model.LibraryBranch;
import java.util.List;

//start L6 Strategy Design Pattern - Strategy interface for branch search
/**
 * Strategy interface for different branch search algorithms.
 */
public interface BranchSearchStrategy {
    List<LibraryBranch> search(String query);
}
//end L6 Strategy Design Pattern
