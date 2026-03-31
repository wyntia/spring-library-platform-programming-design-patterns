package org.pollub.branch.service;

import lombok.RequiredArgsConstructor;
import org.pollub.branch.client.UserServiceClient;
import org.pollub.common.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//Lab5 : ISP 2 Start
public class BranchEmployeesService implements IBranchEmployeesService {
//Lab5 : ISP 2 End

    private final UserServiceClient userServiceClient;
    private final BranchQueryService branchQueryService;

    public List<UserDto> getBranchEmployees(Long branchId) {
        branchQueryService.getBranchById(branchId);
        return userServiceClient.getEmployeesByBranch(branchId);
    }
}

//SRP2 End