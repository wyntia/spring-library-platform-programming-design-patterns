package org.pollub.auth.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

//start L6 Template Method Design Pattern - Abstract class defining the skeleton of the algorithm
/**
 * Abstract class defining the template method for executing WebClient HTTP requests.
 * It encapsulates common error handling, mapping, and blocking operations.
 *
 * @param <T> the type of the expected response body
 */
@Slf4j
public abstract class AbstractWebClientRequest<T> {

    /**
     * The Template Method.
     * Defines the skeleton of the algorithm for making HTTP requests.
     */
    public Optional<T> execute(WebClient.Builder webClientBuilder) {
        try {
            T response = buildRequest(webClientBuilder)
                    .retrieve()
                    .bodyToMono(getResponseType())
                    .block();
            return Optional.ofNullable(response);

        } catch (WebClientResponseException.NotFound e) {
            log.debug("Resource not found during WebClient request: {}", e.getMessage());
            return Optional.empty();
        } catch (WebClientResponseException.Unauthorized e) {
            return handleUnauthorized(e);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    /**
     * Abstract step: building the specific request (URI, method, body, etc.).
     */
    protected abstract WebClient.RequestHeadersSpec<?> buildRequest(WebClient.Builder webClientBuilder);

    /**
     * Abstract step: providing the class type for deserialization.
     */
    protected abstract Class<T> getResponseType();

    /**
     * Hook: allows subclasses to handle general exceptions.
     */
    protected Optional<T> handleException(Exception e) {
        log.error("Error executing WebClient request", e);
        return Optional.empty();
    }

    /**
     * Hook: allows subclasses to handle 401 Unauthorized specific exceptions.
     */
    protected Optional<T> handleUnauthorized(WebClientResponseException.Unauthorized e) {
        return Optional.empty(); // Default behavior
    }
}
