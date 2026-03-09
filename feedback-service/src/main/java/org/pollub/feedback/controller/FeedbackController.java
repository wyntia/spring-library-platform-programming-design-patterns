package org.pollub.feedback.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.pollub.feedback.model.Feedback;
import org.pollub.feedback.model.FeedbackStatus;
import org.pollub.feedback.model.dto.FeedbackAdminDto;
import org.pollub.feedback.model.dto.FeedbackRequestDto;
import org.pollub.feedback.model.dto.FeedbackResponseDto;
import org.pollub.feedback.service.IFeedbackService;
import org.pollub.feedback.command.UpdateFeedbackStatusCommand;
import org.pollub.feedback.command.Command;
import org.pollub.feedback.strategy.IpIdentificationStrategy;
import org.pollub.feedback.strategy.ProxyHeaderIpStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.pollub.common.config.DateTimeProvider;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final IFeedbackService feedbackService;
    private final IpIdentificationStrategy ipStrategy; // L6 Strategy Pattern

    // L6 Strategy Pattern - Constructor injection of strategy with configuration for trusted proxies
    public FeedbackController(IFeedbackService feedbackService,
                              @Value("${app.trusted-proxies:}") Set<String> trustedProxies) {
        this.feedbackService = feedbackService;
        this.ipStrategy = new ProxyHeaderIpStrategy(trustedProxies); // L6 Strategy initialization
    }

    @PostMapping
    public ResponseEntity<FeedbackResponseDto> submitFeedback(
            @Valid @RequestBody FeedbackRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletRequest request
    ) {
        //L6 Strategy Pattern - Identify IP using the selected strategy
        String ipAddress = ipStrategy.identify(request);

        Feedback saved = feedbackService.submitFeedback(dto, ipAddress);

        int[] rateLimitInfo = feedbackService.getRateLimitInfo(ipAddress);
        int remaining = Math.max(0, rateLimitInfo[1] - rateLimitInfo[0] - 1);
        long resetTime = DateTimeProvider.getInstance().now().plusHours(rateLimitInfo[2]).toEpochSecond(ZoneOffset.UTC);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-RateLimit-Limit", String.valueOf(rateLimitInfo[1]));
        headers.add("X-RateLimit-Remaining", String.valueOf(remaining));
        headers.add("X-RateLimit-Reset", String.valueOf(resetTime));

        return ResponseEntity.ok()
                .headers(headers)
                .body(FeedbackResponseDto.success(saved.getId()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<List<FeedbackAdminDto>> getAllFeedbacks(
            @RequestParam(required = false) FeedbackStatus status
    ) {
        List<Feedback> feedbacks = status != null
                ? feedbackService.getFeedbacksByStatus(status)
                : feedbackService.getAllFeedbacks();

        List<FeedbackAdminDto> dtos = feedbacks.stream()
                .map(FeedbackAdminDto::fromEntity)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<FeedbackAdminDto> updateStatus(
            @PathVariable Long id,
            @RequestParam FeedbackStatus status
    ) {
        // start L5 Command
        Command<FeedbackAdminDto> command = new UpdateFeedbackStatusCommand(feedbackService, id, status);
        FeedbackAdminDto dto = command.execute();
        // end L5 Command
        return ResponseEntity.ok(dto);
    }
}