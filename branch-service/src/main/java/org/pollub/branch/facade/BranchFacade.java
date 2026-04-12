package org.pollub.branch.facade;

import lombok.RequiredArgsConstructor;
import org.pollub.branch.model.LibraryBranch;
import org.pollub.branch.model.dto.BranchCreateDto;
import org.pollub.branch.service.BranchCommandService;
import org.pollub.branch.service.BranchEmployeesService;
import org.pollub.branch.service.BranchQueryService;
import org.pollub.branch.service.BranchSearchService;
import org.pollub.common.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
//Lab4 - SRP 2 Start
public class BranchFacade {

    private final BranchQueryService branchQueryService;
    private final BranchCommandService branchCommandService;
    private final BranchSearchService branchSearchService;
    private final BranchEmployeesService branchEmployeesService;

    public List<LibraryBranch> getAllBranches() {
        return branchQueryService.getAllBranches();
    }

    public LibraryBranch getBranchById(Long id) {
        return branchQueryService.getBranchById(id);
    }

    public LibraryBranch getBranchByNumber(String branchNumber) {
        return branchQueryService.getBranchByNumber(branchNumber);
    }

    public List<LibraryBranch> searchBranches(String query) {
        return branchSearchService.searchBranches(query);
    }

    public LibraryBranch createBranch(BranchCreateDto dto) {
        return branchCommandService.createBranch(dto);
    }

    public LibraryBranch updateBranch(Long id, BranchCreateDto dto) {
        return branchCommandService.updateBranch(id, dto);
    }

    public void deleteBranch(Long id) {
        branchCommandService.deleteBranch(id);
    }

    public List<UserDto> getBranchEmployees(Long branchId) {
        return branchEmployeesService.getBranchEmployees(branchId);
    }

    public List<LibraryBranch> getBranchesByIds(List<Long> branchIds) {
        return branchQueryService.getBranchesByIds(branchIds);
    }
}
//SRP2 End