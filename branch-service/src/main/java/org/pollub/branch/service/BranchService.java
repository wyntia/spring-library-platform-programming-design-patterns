package org.pollub.branch.service;

import lombok.RequiredArgsConstructor;
import org.pollub.branch.facade.BranchFacade;
import org.pollub.branch.model.LibraryBranch;
import org.pollub.branch.model.dto.BranchCreateDto;
import org.pollub.common.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("baseBranchService")
@Transactional
@RequiredArgsConstructor

public class BranchService implements IBranchService {

    private final BranchFacade branchFacade;

    public List<LibraryBranch> getAllBranches() {
        return branchFacade.getAllBranches();
    }
    
    public LibraryBranch getBranchById(Long id) {
        return branchFacade.getBranchById(id);
    }
    
    public LibraryBranch getBranchByNumber(String branchNumber) {
        return branchFacade.getBranchByNumber(branchNumber);
    }
    
    public List<LibraryBranch> searchBranches(String query) {
        return branchFacade.searchBranches(query);
    }

    public LibraryBranch createBranch(BranchCreateDto dto) {
        return branchFacade.createBranch(dto);
    }

    public LibraryBranch updateBranch(Long id, BranchCreateDto dto) {
        return branchFacade.updateBranch(id, dto);
    }
    
    public void deleteBranch(Long id) {
        branchFacade.deleteBranch(id);
    }
    
    /**
     * Get employees assigned to this branch from user-service
     */
    public List<UserDto> getBranchEmployees(Long branchId) {
        return branchFacade.getBranchEmployees(branchId);
    }

    /**
     * Get multiple branches by IDs
     */
    public List<LibraryBranch> getBranchesByIds(List<Long> branchIds) {
        return branchFacade.getBranchesByIds(branchIds);
    }

    //Lab5 : Liskov 2 Start
    @Override
    public java.util.Map<String, Object> getBranchHierarchy() {
        branch.BranchComponent countryGroup = new branch.BranchGroup("Miejska Biblioteka Publiczna w Lublinie");

        branch.BranchComponent northRegion = new branch.BranchGroup("Śródmieście i Północ");
        branch.BranchComponent southRegion = new branch.BranchGroup("Południe");

        northRegion.addChild(new branch.Branch("Filia nr 1 - Śródmieście"));
        northRegion.addChild(new branch.Branch("Filia nr 2 - Czechów"));

        southRegion.addChild(new branch.Branch("Filia nr 3 - Czuby"));

        countryGroup.addChild(northRegion);
        countryGroup.addChild(southRegion);

        return buildHierarchyMap(countryGroup);
    }

    private java.util.Map<String, Object> buildHierarchyMap(branch.BranchComponent component) {
        return java.util.Map.of(
            "name", component.getName(),
            "children", component.getChildren().stream()
                .map(this::buildHierarchyMap)
                .collect(java.util.stream.Collectors.toList())
        );
    }
    //Lab5 : Liskov 2 End
}
