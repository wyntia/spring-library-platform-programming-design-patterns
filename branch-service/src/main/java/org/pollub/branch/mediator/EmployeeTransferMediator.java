package org.pollub.branch.mediator;

import lombok.RequiredArgsConstructor;
import org.pollub.branch.model.LibraryBranch;
import org.pollub.branch.repository.BranchRepository;
import org.pollub.branch.client.UserServiceClient;
import org.pollub.common.dto.UserDto;
import org.pollub.common.exception.ResourceNotFoundException;
import org.pollub.branch.service.NotificationService;
import org.pollub.branch.service.AuditService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeTransferMediator {
    private final BranchRepository branchRepository;
    private final UserServiceClient userServiceClient;
    private final NotificationService notificationService;
    private final AuditService auditService;

    //start L5 Mediator
    public boolean transferEmployee(Long employeeId, Long targetBranchId) {
        LibraryBranch branch = branchRepository.findById(targetBranchId)
            .orElseThrow(() -> new ResourceNotFoundException("Branch", targetBranchId));
        if (!branch.hasFreeSlot()) {
            notificationService.notifyAdmin("Brak wolnych miejsc w oddziale " + branch.getName());
            return false;
        }

        UserDto employee = userServiceClient.getUserById(employeeId);
        if (employee.getEmployeeBranchId() != null && employee.getEmployeeBranchId().equals(targetBranchId)) {
            notificationService.notifyUser(employeeId, "Jesteś już przypisany do tego oddziału.");
            return false;
        }

        userServiceClient.updateEmployeeBranch(employeeId, targetBranchId);

        notificationService.notifyUser(employeeId, "Zostałeś przeniesiony do oddziału " + branch.getName());
        notificationService.notifyAdmin("Pracownik " + employee.getName() + " został przeniesiony.");

        auditService.logTransfer(employeeId, targetBranchId);

        return true;
    }
    //end L5 Mediator
}
