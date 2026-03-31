package org.pollub.catalog.service;

import org.pollub.catalog.model.CopyStatus;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

//Lab4 OCP Start
public final class InventoryTransitionRuleDictionary {

    private static final Map<InventoryOperation, TransitionRule> TRANSITION_RULES = createTransitionRules();

    private InventoryTransitionRuleDictionary() {
    }

    public static TransitionRule getRule(InventoryOperation operation) {
        TransitionRule transitionRule = TRANSITION_RULES.get(operation);
        if (transitionRule == null) {
            throw new IllegalStateException("Missing transition rule for operation: " + operation);
        }
        return transitionRule;
    }

    private static Map<InventoryOperation, TransitionRule> createTransitionRules() {
        Map<InventoryOperation, TransitionRule> transitionRules = new EnumMap<>(InventoryOperation.class);
        transitionRules.put(InventoryOperation.RENT, new TransitionRule(EnumSet.of(CopyStatus.AVAILABLE, CopyStatus.RESERVED), CopyStatus.RENTED));
        transitionRules.put(InventoryOperation.RETURN, new TransitionRule(EnumSet.of(CopyStatus.RENTED), CopyStatus.AVAILABLE));
        transitionRules.put(InventoryOperation.RESERVE, new TransitionRule(EnumSet.of(CopyStatus.AVAILABLE), CopyStatus.RESERVED));
        transitionRules.put(InventoryOperation.CANCEL_RESERVATION, new TransitionRule(EnumSet.of(CopyStatus.RESERVED), CopyStatus.AVAILABLE));
        transitionRules.put(InventoryOperation.EXTEND, new TransitionRule(EnumSet.of(CopyStatus.RENTED), CopyStatus.RENTED));
        return transitionRules;
    }

    public record TransitionRule(Set<CopyStatus> allowedStatuses, CopyStatus targetStatus) {
    }
}
//Lab4 OCP End