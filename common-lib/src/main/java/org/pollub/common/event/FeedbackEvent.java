package org.pollub.common.event;

import java.time.LocalDateTime;

//L6 Observer pattern - Event class for feedback status changes
/**
 * Event representing a change in feedback status.
 * Used to notify observers about feedback operations.
 */
public record FeedbackEvent(
    String eventType,      // "FEEDBACK_SUBMITTED", "STATUS_CHANGED_RESOLVED", "STATUS_CHANGED_DISMISSED"
    Long feedbackId,
    String category,
    String message,
    String ipAddress,
    String statusBefore,
    String statusAfter,
    LocalDateTime timestamp
) {
}

