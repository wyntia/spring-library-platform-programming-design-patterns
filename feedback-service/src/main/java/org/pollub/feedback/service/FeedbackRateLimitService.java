package org.pollub.feedback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pollub.common.config.DateTimeProvider;
import org.pollub.feedback.repository.IFeedbackRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
//Lab5 : ISP 3 Start
public class FeedbackRateLimitService implements IFeedbackRateLimitService {
//Lab5 : ISP 3 End

    private final IFeedbackRepository feedbackRepository;

    public boolean isRateLimitExceeded(String ipAddress) {
        if (ipAddress == null || ipAddress.isBlank()) {
            log.warn("Rate limit check failed: IP address unknown, blocking request");
            return true;
        }

        //L4 - OCP 3 Start
        FeedbackRateLimitRuleDictionary.RateLimitRule effectiveRateLimitRule = resolveEffectiveRateLimitRuleForCurrentRequestContext();
        LocalDateTime windowStart = DateTimeProvider.getInstance().now().minusHours(effectiveRateLimitRule.windowHours());
        long count = feedbackRepository.countByIpAddressSince(ipAddress, windowStart);

        return count >= effectiveRateLimitRule.maxRequests();
        //L4 - OCP 3 END
    }

    public int[] getRateLimitInfo(String ipAddress) {
        //L4 - OCP 3 Start
        FeedbackRateLimitRuleDictionary.RateLimitRule effectiveRateLimitRule = resolveEffectiveRateLimitRuleForCurrentRequestContext();
        int currentCount = 0;
        if (ipAddress != null && !ipAddress.isBlank()) {
            LocalDateTime windowStart = DateTimeProvider.getInstance().now().minusHours(effectiveRateLimitRule.windowHours());
            currentCount = (int) feedbackRepository.countByIpAddressSince(ipAddress, windowStart);
        }
        return new int[] { currentCount, effectiveRateLimitRule.maxRequests(), effectiveRateLimitRule.windowHours() };
        //L4 - OCP 3 END
    }

    private FeedbackRateLimitRuleDictionary.RateLimitRule resolveEffectiveRateLimitRuleForCurrentRequestContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!isAuthenticatedUser(authentication)) {
            return FeedbackRateLimitRuleDictionary.getGuestRule();
        }

        Set<String> normalizedRoles = extractNormalizedRoles(authentication);
        FeedbackRateLimitRuleDictionary.RateLimitRule roleSpecificRule = resolveRoleSpecificRateLimitRuleUsingDictionaryMappings(normalizedRoles);
        if (roleSpecificRule != null) {
            return roleSpecificRule;
        }

        return FeedbackRateLimitRuleDictionary.getAuthenticatedRule();
    }

    // Lab7 : Programowanie funkcyjne — strumienie na kolekcjach 3/3 (feedback) Start
    private FeedbackRateLimitRuleDictionary.RateLimitRule resolveRoleSpecificRateLimitRuleUsingDictionaryMappings(Set<String> normalizedRoles) {
        return normalizedRoles.stream()
                .map(FeedbackRateLimitRuleDictionary::getRoleSpecificRule)
                .filter(rule -> rule != null)
                .findFirst()
                .orElse(null);
    }
    // Lab7 : Programowanie funkcyjne — strumienie na kolekcjach 3/3 (feedback) End

    private Set<String> extractNormalizedRoles(Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return Set.of();
        }
        return authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority().toUpperCase(Locale.ROOT))
                .collect(Collectors.toSet());
    }

    private boolean isAuthenticatedUser(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        if (!authentication.isAuthenticated()) {
            return false;
        }
        if (authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        return principal instanceof UserDetails;
    }
}
//SRP3 End
