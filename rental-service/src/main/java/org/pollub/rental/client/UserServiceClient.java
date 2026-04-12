package org.pollub.rental.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pollub.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient for communicating with user-service to fetch user emails.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserServiceClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${services.user.url:http://user-service}")
    private String userServiceUrl;

    /**
     * Get user email by user ID.
     *
     * @param userId the user ID
     * @return user email
     * @throws ServiceException if the user cannot be loaded or has no email
     */
    //Lab6 : Wyjątki zamiast kodów błędów — przykład 3 (email z user-service) Start
    public String getUserEmail(Long userId) {
        try {
            UserEmailResponse response = webClientBuilder.build()
                    .get()
                    .uri(userServiceUrl + "/api/users/{id}", userId)
                    .retrieve()
                    .bodyToMono(UserEmailResponse.class)
                    .block();
            if (response == null || response.getEmail() == null || response.getEmail().isBlank()) {
                throw new ServiceException("user-service", "No email for user " + userId);
            }
            return response.getEmail();
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Failed to get email for user {}: {}", userId, e.getMessage());
            throw new ServiceException("user-service", "Failed to get email for user " + userId, e);
        }
    }
    //Lab6 : Wyjątki zamiast kodów błędów — przykład 3 (email z user-service) Stop

    /**
     * Simple DTO for extracting email from user response.
     */
    @lombok.Data
    public static class UserEmailResponse {
        private Long id;
        private String email;
        private String firstName;
        private String lastName;
    }
}
