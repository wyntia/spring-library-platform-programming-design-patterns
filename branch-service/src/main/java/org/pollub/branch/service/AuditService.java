package org.pollub.branch.service;

import org.springframework.stereotype.Component;

@Component
public class AuditService {
    public void logTransfer(Long employeeId, Long branchId) {
        System.out.println("[AUDIT] Transfer: employee=" + employeeId + ", branch=" + branchId);
    }
}
