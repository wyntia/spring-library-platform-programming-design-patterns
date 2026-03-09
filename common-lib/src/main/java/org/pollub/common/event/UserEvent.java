package org.pollub.common.event;

import java.time.LocalDateTime;

//L6 Observer pattern - Event class for user status changes
/**
 * Event representing a change in user status.
 * Used to notify observers about user operations.
 */
public record UserEvent(
    String eventType,      // "USER_CREATED", "USER_UPDATED", "ROLES_CHANGED", "PASSWORD_CHANGED", etc.
    Long userId,
    String username,
    String email,
    String action,         // Details of the action (e.g., old role -> new role)
    LocalDateTime timestamp
) {
}

