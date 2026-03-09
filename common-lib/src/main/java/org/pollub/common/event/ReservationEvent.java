package org.pollub.common.event;

import java.time.LocalDateTime;

//L3 Observer pattern - Event class for reservation status changes
/**
 * Event representing a change in reservation status.
 * Used to notify observers about reservation operations.
 */
public record ReservationEvent(
    String eventType,      // "CREATED", "FULFILLED", "CANCELLED", "EXPIRED"
    Long reservationId,
    Long itemId,
    Long userId,
    LocalDateTime timestamp
) {
}

