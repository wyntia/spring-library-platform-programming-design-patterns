package org.pollub.common.event;

import java.time.LocalDateTime;

//L3 Observer pattern - Event class for branch inventory changes
/**
 * Event representing a change in branch inventory status.
 * Used to notify observers about inventory operations.
 */
public record BranchInventoryEvent(
    String eventType,      // "RENT", "RETURN", "RESERVE", "CANCEL_RESERVATION", "EXTEND", "STATUS_CHANGED"
    Long itemId,
    Long branchId,
    Long userId,
    LocalDateTime timestamp
) {
}

