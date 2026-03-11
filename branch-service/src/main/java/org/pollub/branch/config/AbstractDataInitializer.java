package org.pollub.branch.config;

import lombok.extern.slf4j.Slf4j;
import org.pollub.branch.model.LibraryBranch;
import org.pollub.branch.repository.BranchRepository;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

// L3 Template Method Design Pattern
/**
 * Abstract class defining the skeleton algorithm for data initialization.
 */
@Slf4j
public abstract class AbstractDataInitializer implements CommandLineRunner {

    protected final BranchRepository branchRepository;

    protected AbstractDataInitializer(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    /**
     * The Template Method.
     * It defines the fixed algorithm skeleton: check if data exists, fetch new data, and save it.
     */
    @Override
    public final void run(String... args) {
        if (branchRepository.count() == 0) {
            log.info("No branches found. Starting data initialization...");

            // Abstract step: fetch data from a specific source
            List<LibraryBranch> branches = fetchData();

            if (branches != null && !branches.isEmpty()) {
                branchRepository.saveAll(branches);
                log.info("Successfully populated {} branches.", branches.size());
            } else {
                log.warn("No data provided to initialize.");
            }
        } else {
            log.info("Branches already exist in the database. Initialization skipped.");
        }
    }

    /**
     * Abstract hook method to provide the actual data to be saved.
     * Can be implemented to read from hardcoded lists, CSV, APIs, etc.
     */
    protected abstract List<LibraryBranch> fetchData();
}
