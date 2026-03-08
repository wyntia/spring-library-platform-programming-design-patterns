package org.pollub.branch.mediator;

import lombok.RequiredArgsConstructor;
import org.pollub.branch.client.UserServiceClient;
import org.pollub.branch.model.LibraryBranch;
import org.pollub.branch.model.dto.BranchCreateDto;
import org.pollub.branch.repository.BranchRepository;
import org.pollub.common.dto.UserDto;
import org.pollub.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import java.util.List;

//start L5 Mediator
@Component
@RequiredArgsConstructor
public class BranchMediatorImpl implements BranchMediator {
    private final BranchRepository branchRepository;
    private final UserServiceClient userServiceClient;

    @Override
    public List<UserDto> getBranchEmployees(Long branchId) {
        // Verify branch exists
        branchRepository.findById(branchId)
            .orElseThrow(() -> new ResourceNotFoundException("LibraryBranch", branchId));
        return userServiceClient.getEmployeesByBranch(branchId);
    }

    @Override
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

    @Override
    public LibraryBranch updateBranch(Long id, BranchCreateDto dto) {
        LibraryBranch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LibraryBranch", id));
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
}
//end L5 Mediator
