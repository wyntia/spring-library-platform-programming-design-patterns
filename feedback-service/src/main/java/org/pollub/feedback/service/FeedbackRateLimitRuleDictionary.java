package org.pollub.feedback.service;

import java.util.Map;

public final class FeedbackRateLimitRuleDictionary {

    private static final String SUBJECT_GUEST = "GUEST";
    private static final String SUBJECT_AUTHENTICATED = "AUTHENTICATED";

    private static final Map<String, RateLimitRule> SUBJECT_RATE_LIMIT_RULES = Map.of(
            SUBJECT_GUEST, new RateLimitRule(3, 1),
            SUBJECT_AUTHENTICATED, new RateLimitRule(8, 1)
    );

    private static final Map<String, RateLimitRule> ROLE_SPECIFIC_RATE_LIMIT_RULES = Map.of(
            "ROLE_ADMIN", new RateLimitRule(20, 1),
            "ROLE_LIBRARIAN", new RateLimitRule(15, 1)
    );

    private FeedbackRateLimitRuleDictionary() {
    }

    public static RateLimitRule getGuestRule() {
        return SUBJECT_RATE_LIMIT_RULES.get(SUBJECT_GUEST);
    }

    public static RateLimitRule getAuthenticatedRule() {
        return SUBJECT_RATE_LIMIT_RULES.get(SUBJECT_AUTHENTICATED);
    }

    public static RateLimitRule getRoleSpecificRule(String normalizedRole) {
        return ROLE_SPECIFIC_RATE_LIMIT_RULES.get(normalizedRole);
    }

    public record RateLimitRule(int maxRequests, int windowHours) {
    }
}
