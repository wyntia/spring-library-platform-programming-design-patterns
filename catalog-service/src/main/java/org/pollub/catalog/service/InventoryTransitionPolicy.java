package org.pollub.catalog.service;

import org.pollub.catalog.model.CopyStatus;
import org.springframework.stereotype.Component;

//Lab4 OCP Start
@Component
public class InventoryTransitionPolicy {

    public CopyStatus resolveTargetStatus(InventoryOperation operation) {
        return InventoryTransitionRuleDictionary.getRule(operation).targetStatus();
    }

    public void throwIfOperationNotAllowed(CopyStatus currentStatus, InventoryOperation operation, String errorMessagePrefix) {
        InventoryTransitionRuleDictionary.TransitionRule transitionRule = InventoryTransitionRuleDictionary.getRule(operation);
        if (!transitionRule.allowedStatuses().contains(currentStatus)) {
            throw new IllegalStateException(errorMessagePrefix + currentStatus);
        }
    }

    public boolean isOperationAllowed(CopyStatus currentStatus, InventoryOperation operation) {
        InventoryTransitionRuleDictionary.TransitionRule transitionRule = InventoryTransitionRuleDictionary.getRule(operation);
        return transitionRule.allowedStatuses().contains(currentStatus);
    }
}
//Lab4 OCP End