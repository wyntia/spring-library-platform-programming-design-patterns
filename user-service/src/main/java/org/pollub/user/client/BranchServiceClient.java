package org.pollub.user.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pollub.common.dto.BranchDto;
import org.pollub.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class BranchServiceClient implements IBranchServiceClient {
    
    private final WebClient.Builder webClientBuilder;
    
    @Value("${services.branch.url:http://branch-service}")
    private String branchServiceUrl;
    
    //Lab6 : Wyjątki zamiast kodów błędów — przykład 2 (branch-service) Start
    public Optional<BranchDto> getBranchById(Long id) {
        try {
            BranchDto branch = webClientBuilder.build()
                    .get()
                    .uri(branchServiceUrl + "/api/branches/" + id)
                    .retrieve()
                    .bodyToMono(BranchDto.class)
                    .block();
            return Optional.ofNullable(branch);
        } catch (WebClientResponseException e) {
            HttpStatusCode status = e.getStatusCode();
            if (status.value() == 404) {
                log.debug("Branch not found for id: {}", id);
                return Optional.empty();
            }
            log.error("Error fetching branch with id: {}", id, e);
            throw new ServiceException("branch-service", "Failed to get branch " + id, e);
        } catch (Exception e) {
            log.error("Error fetching branch with id: {}", id, e);
            throw new ServiceException("branch-service", "Failed to get branch " + id, e);
        }
    }
    //Lab6 : Wyjątki zamiast kodów błędów — przykład 2 (branch-service) Stop
}
