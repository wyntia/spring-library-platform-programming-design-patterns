//start L3 Decorator
package org.pollub.branch.service;

import org.pollub.branch.model.LibraryBranch;
import org.pollub.branch.model.dto.BranchCreateDto;
import org.pollub.common.dto.UserDto;
import java.util.List;

/**
 * A logging decorator for the branch service.
 */
public class BranchServiceLoggingDecorator implements IBranchService {
    private final IBranchService delegate;
    public BranchServiceLoggingDecorator(IBranchService delegate) {
        this.delegate = delegate;
    }
    @Override
    public List<LibraryBranch> getAllBranches() {
        System.out.println("[LOG] Pobieranie listy oddziałów");
        return delegate.getAllBranches();
    }
    @Override
    public LibraryBranch getBranchById(Long id) {
        System.out.println("[LOG] Pobieranie oddziału o ID: " + id);
        return delegate.getBranchById(id);
    }
    @Override
    public LibraryBranch getBranchByNumber(String branchNumber) {
        System.out.println("[LOG] Pobieranie oddziału o numerze: " + branchNumber);
        return delegate.getBranchByNumber(branchNumber);
    }
    @Override
    public List<LibraryBranch> searchBranches(String query) {
        System.out.println("[LOG] Wyszukiwanie oddziałów: " + query);
        return delegate.searchBranches(query);
    }
    @Override
    public LibraryBranch createBranch(BranchCreateDto dto) {
        System.out.println("[LOG] Tworzenie oddziału: " + dto.getName());
        return delegate.createBranch(dto);
    }
    @Override
    public LibraryBranch updateBranch(Long id, BranchCreateDto dto) {
        System.out.println("[LOG] Aktualizacja oddziału o ID: " + id);
        return delegate.updateBranch(id, dto);
    }
    @Override
    public void deleteBranch(Long id) {
        System.out.println("[LOG] Usuwanie oddziału o ID: " + id);
        delegate.deleteBranch(id);
    }
    @Override
    public List<UserDto> getBranchEmployees(Long branchId) {
        System.out.println("[LOG] Pobieranie pracowników oddziału o ID: " + branchId);
        return delegate.getBranchEmployees(branchId);
    }
    @Override
    public List<LibraryBranch> getBranchesByIds(List<Long> branchIds) {
        System.out.println("[LOG] Pobieranie wielu oddziałów po ID: " + branchIds);
        return delegate.getBranchesByIds(branchIds);
    }
}
//end L3 Decorator
