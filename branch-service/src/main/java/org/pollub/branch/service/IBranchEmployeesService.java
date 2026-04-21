package org.pollub.branch.service;

import org.pollub.common.dto.UserDto;
import java.util.List;

//Lab5 : ISP 2 Start
public interface IBranchEmployeesService {
    List<UserDto> getBranchEmployees(Long branchId);
}
//Lab5 : ISP 2 End
