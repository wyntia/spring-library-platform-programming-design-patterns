package org.pollub.common.function;

//Lab7 : Interfejsy funkcyjne i lambda — definicja 3/3
/**
 * Masks or redacts IP addresses for privacy in admin-facing DTOs.
 */
@FunctionalInterface
public interface FeedbackIpAnonymizer {

    String anonymize(String ipAddress);
}
