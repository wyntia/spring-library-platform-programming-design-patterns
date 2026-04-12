package org.pollub.user.service;

import org.pollub.common.dto.BranchDto;
import org.pollub.user.model.User;
//Lab5 : ISP 1 Start

public interface IEmployeeBranchAssignmentService {
    User updateEmployeeBranch(Long userId, Long branchId);
    Long getEmployeeBranchId(Long userId);
    BranchDto getEmployeeBranch(String username);
    BranchDto getEmployeeBranchById(Long userId);
}
//Lab5 : ISP 1 End