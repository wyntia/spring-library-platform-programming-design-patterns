package org.pollub.common.event;

import java.time.LocalDateTime;

//L3 Observer pattern - Event class for rental status changes
/**
 * Event representing a change in rental status.
 * Used to notify observers about rental operations.
 */
public record RentalEvent(
    String eventType,      // "CREATED", "RETURNED", "EXTENDED", "OVERDUE"
    Long rentalId,
    Long itemId,
    Long userId,
    LocalDateTime timestamp
) {
}

