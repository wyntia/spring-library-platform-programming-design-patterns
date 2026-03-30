package org.pollub.branch.service;

import lombok.RequiredArgsConstructor;
import org.pollub.branch.model.LibraryBranch;
import org.pollub.branch.model.dto.BranchCreateDto;
import org.pollub.branch.repository.BranchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
//Lab4 - SRP 2 Start
public class BranchCommandService {

    private final BranchRepository branchRepository;
    private final BranchQueryService branchQueryService;

    public LibraryBranch createBranch(BranchCreateDto dto) {
        LibraryBranch branch = LibraryBranch.builder()
                .branchNumber(dto.getBranchNumber())
                .name(dto.getName())
                .city(dto.getCity())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .openingHours(dto.getOpeningHours())
                .build();
        return branchRepository.save(branch);
    }

    public LibraryBranch updateBranch(Long id, BranchCreateDto dto) {
        LibraryBranch branch = branchQueryService.getBranchById(id);
        branch.setBranchNumber(dto.getBranchNumber());
        branch.setName(dto.getName());
        branch.setCity(dto.getCity());
        branch.setAddress(dto.getAddress());
        branch.setLatitude(dto.getLatitude());
        branch.setLongitude(dto.getLongitude());
        branch.setPhone(dto.getPhone());
        branch.setEmail(dto.getEmail());
        branch.setOpeningHours(dto.getOpeningHours());
        return branchRepository.save(branch);
    }

    public void deleteBranch(Long id) {
        branchQueryService.getBranchById(id);
        branchRepository.deleteById(id);
    }
}
//SRP2 End
