package org.pollub.auth.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pollub.auth.template.AbstractWebClientRequest;
import org.pollub.common.dto.BranchDto;
import org.pollub.common.dto.UserDto;
import org.pollub.common.exception.ServiceException;
import org.pollub.common.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Optional;

// L6 Template Method Usage Context
/**
 * WebClient for communicating with user-service.
 * Refactored to use the Template Method pattern for executing HTTP requests.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserServiceClient {
    
    private final WebClient.Builder webClientBuilder;
    
    @Value("${services.user.url:http://user-service}")
    private String userServiceUrl;

    public Optional<UserDto> findByEmail(String email) {
        //L6 Template Method usage for fetching user by email
        return new AbstractWebClientRequest<UserDto>() {
            @Override
            protected WebClient.RequestHeadersSpec<?> buildRequest(WebClient.Builder builder) {
                return builder.build().get().uri(userServiceUrl + "/api/users/email/{email}", email);
            }
            @Override
            protected Class<UserDto> getResponseType() { return UserDto.class; }
            @Override
            protected Optional<UserDto> handleException(Exception e) {
                log.error("Error fetching user by email", e);
                throw new ServiceException("user-service", "Failed to fetch user by email", e);
            }
        }.execute(webClientBuilder);
    }

    public Optional<UserDto> findByUsername(String username) {
        //L6 Template Method usage for fetching user by username
        return new AbstractWebClientRequest<UserDto>() {
            @Override
            protected WebClient.RequestHeadersSpec<?> buildRequest(WebClient.Builder builder) {
                return builder.build().get().uri(userServiceUrl + "/api/users/username/{username}", username);
            }
            @Override
            protected Class<UserDto> getResponseType() { return UserDto.class; }
            @Override
            protected Optional<UserDto> handleException(Exception e) {
                log.error("Error fetching user by username", e);
                throw new ServiceException("user-service", "Failed to fetch user by username", e);
            }
        }.execute(webClientBuilder);
    }

    public UserDto createUser(UserDto userDto) {
        return webClientBuilder.build()
                .post()
                .uri(userServiceUrl + "/api/users")
                .bodyValue(userDto)
                .retrieve()
                .onStatus(
                        status -> status.isSameCodeAs(HttpStatus.CONFLICT),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorMessage -> Mono.error(new UserAlreadyExistsException(errorMessage)))
                )
                .bodyToMono(UserDto.class)
                .block();
    }

    public Optional<UserDto> validateCredentials(String usernameOrEmail, String password) {
        //L6 Template Method usage for validating credentials
        return new AbstractWebClientRequest<UserDto>() {
            @Override
            protected WebClient.RequestHeadersSpec<?> buildRequest(WebClient.Builder builder) {
                return builder.build().post()
                        .uri(userServiceUrl + "/api/users/validate")
                        .bodyValue(new CredentialsDto(usernameOrEmail, password));
            }
            @Override
            protected Class<UserDto> getResponseType() { return UserDto.class; }
            @Override
            protected Optional<UserDto> handleException(Exception e) {
                log.error("Error validating credentials", e);
                throw new ServiceException("user-service", "Failed to validate credentials", e);
            }
            @Override
            protected Optional<UserDto> handleUnauthorized(WebClientResponseException.Unauthorized e) {
                return Optional.empty();
            }
        }.execute(webClientBuilder);
    }


    // Simple DTO for credentials
    public record CredentialsDto(String usernameOrEmail, String password) {}

    public Optional<BranchDto> getEmployeeBranch(String username) {
        //L6 Template Method usage for fetching employee branch
        return new AbstractWebClientRequest<BranchDto>() {
            @Override
            protected WebClient.RequestHeadersSpec<?> buildRequest(WebClient.Builder builder) {
                return builder.build().get().uri(userServiceUrl + "/api/users/username/{username}/branch", username);
            }
            @Override
            protected Class<BranchDto> getResponseType() { return BranchDto.class; }
            @Override
            protected Optional<BranchDto> handleException(Exception e) {
                log.error("Error fetching employee branch for username: {}", username, e);
                return Optional.empty();
            }
        }.execute(webClientBuilder);
    }

    /**
     * Reset password for a user identified by email and PESEL.
     * Returns the new temporary password if successful.
     */
    public Optional<ResetPasswordResponseDto> resetPassword(String email, String pesel) {
        //L6 Template Method usage for reset password request
        return new AbstractWebClientRequest<ResetPasswordResponseDto>() {
            @Override
            protected WebClient.RequestHeadersSpec<?> buildRequest(WebClient.Builder builder) {
                return builder.build().post()
                        .uri(userServiceUrl + "/api/users/reset-password")
                        .bodyValue(new ResetPasswordRequestDto(email, pesel));
            }
            @Override
            protected Class<ResetPasswordResponseDto> getResponseType() { return ResetPasswordResponseDto.class; }
            @Override
            protected Optional<ResetPasswordResponseDto> handleException(Exception e) {
                log.error("Error resetting password for email: {}", email, e);
                return Optional.empty();
            }
        }.execute(webClientBuilder);
    }

    // DTOs for reset password
    public record ResetPasswordRequestDto(String email, String pesel) {}

    public record ResetPasswordResponseDto(String email, String temporaryPassword, boolean success, String message) {}
}
