package org.pollub.feedback.model.dto;



import org.pollub.common.function.FeedbackIpAnonymizer;
import org.pollub.feedback.model.Feedback;
import org.pollub.feedback.model.FeedbackCategory;
import org.pollub.feedback.model.FeedbackStatus;

import java.time.LocalDateTime;

/**
 * Admin DTO for viewing feedbacks without exposing sensitive data.
 * Excludes: full User entity, raw IP address.
 */
public record FeedbackAdminDto(
        Long id,
        FeedbackCategory category,
        String message,
        String pageUrl,
        FeedbackStatus status,
        LocalDateTime createdAt,
        LocalDateTime resolvedAt,
        String maskedIp
) {
    /**
     * Convert entity to admin DTO with sensitive data masked.
     */
    public static FeedbackAdminDto fromEntity(Feedback feedback) {
        //Lab7 : Interfejsy funkcyjne i lambda — użycie 3/3 (feedback)
        return toAdminDto(
                feedback,
                ip -> {
                    if (ip == null || ip.isBlank()) {
                        return "unknown";
                    }
                    int lastDot = ip.lastIndexOf('.');
                    if (lastDot > 0) {
                        return ip.substring(0, lastDot) + ".***";
                    }
                    int lastColon = ip.lastIndexOf(':');
                    if (lastColon > 0) {
                        return ip.substring(0, lastColon) + ":****";
                    }
                    return "***";
                });
    }

    private static FeedbackAdminDto toAdminDto(Feedback feedback, FeedbackIpAnonymizer ipAnonymizer) {
        return new FeedbackAdminDto(
                feedback.getId(),
                feedback.getCategory(),
                feedback.getMessage(),
                feedback.getPageUrl(),
                feedback.getStatus(),
                feedback.getCreatedAt(),
                feedback.getResolvedAt(),
                ipAnonymizer.anonymize(feedback.getIpAddress())
        );
    }
}
