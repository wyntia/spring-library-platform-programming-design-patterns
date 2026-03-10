package org.pollub.feedback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pollub.common.Observer;
import org.pollub.common.Subject;
import org.pollub.common.config.DateTimeProvider;
import org.pollub.common.event.FeedbackEvent;
import org.pollub.common.exception.ResourceNotFoundException;
import org.pollub.feedback.interpreter.FeedbackSearchExpression;
import org.pollub.feedback.interpreter.StatusExpression;
import org.pollub.feedback.model.Feedback;
import org.pollub.feedback.model.FeedbackStatus;
import org.pollub.feedback.model.dto.FeedbackRequestDto;
import org.pollub.feedback.repository.IFeedbackRepository;
import org.pollub.feedback.visitor.SecuritySanitizationVisitor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FeedbackService implements IFeedbackService, Subject {
        // --- Notification decorator ---
        private final notification.NotificationComponent notificationSender =
                new notification.LoggingNotificationDecorator(
                    new notification.NotificationTemplateDecorator(
                        new notification.NotificationGroup() {{
                            add(new notification.EmailNotification("user@example.com"));
                            add(new notification.SmsNotification("123-456-789"));
                        }},
                        "[Biblioteka Miejska]", "Dziękujemy za korzystanie z naszych usług!"
                    )
                );
    
    private final IFeedbackRepository feedbackRepository;
    private final List<Observer> observers = new ArrayList<>();

    /**
     * Maximum number of feedback submissions per IP within the rate limit window.
     */
    @Value("${feedback.rate-limit.max-requests:3}")
    private int maxRequestsPerWindow;

    /**
     * Rate limit window in hours.
     */
    @Value("${feedback.rate-limit.window-hours:1}")
    private int windowHours;

    @Override
    @Transactional
    public Feedback submitFeedback(FeedbackRequestDto dto , String ipAddress) {
        Feedback feedback = Feedback.builder()
                .category(dto.category())
                .message(dto.message()) // L6 refactor - sanitize message to prevent XSS
                .pageUrl(dto.pageUrl())
                .ipAddress(ipAddress)
                .createdAt(DateTimeProvider.getInstance().now())
                .status(FeedbackStatus.NEW)
                .build();

        // start L6 Visitor pattern refactor
        // Use visitor to sanitize data instead of a private method
        SecuritySanitizationVisitor sanitizer = new SecuritySanitizationVisitor();
        feedback.accept(sanitizer);
        // end L6 Visitor pattern refactor

        Feedback saved = feedbackRepository.save(feedback);
        log.info("Feedback submitted: id={}, category={}",
                saved.getId(), saved.getCategory());

        // Notify observers about feedback submission
        notifyObservers(new FeedbackEvent(
            "FEEDBACK_SUBMITTED",
            saved.getId(),
            saved.getCategory().name(),
            saved.getMessage(),
            ipAddress,
            null,
            FeedbackStatus.NEW.name(),
            DateTimeProvider.getInstance().now()
        ));

        // Wysyłka powiadomienia (dekorator + composite)
        notificationSender.send("Nowe zgłoszenie: " + saved.getMessage());

        return saved;
    }

    @Override
    public boolean isRateLimitExceeded(String ipAddress) {
        if (ipAddress == null || ipAddress.isBlank()) {
            log.warn("Rate limit check failed: IP address unknown, blocking request");
            return true; // Block if IP cannot be determined (security measure)
        }

        LocalDateTime windowStart = DateTimeProvider.getInstance().now().minusHours(windowHours);
        long count = feedbackRepository.countByIpAddressSince(ipAddress, windowStart);

        return count >= maxRequestsPerWindow;
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public List<Feedback> getFeedbacksByStatus(FeedbackStatus status) {
        //start L3 Interpreter
        List<Feedback> allFeedbacks = feedbackRepository.findAllByOrderByCreatedAtDesc();
        FeedbackSearchExpression expr = new StatusExpression(status);
        List<Feedback> filtered = expr.interpret(allFeedbacks);
        //end L3 Interpreter
        return filtered;
    }

    @Override
    @Transactional
    public Feedback updateStatus(Long feedbackId, FeedbackStatus newStatus) {
        //L6 Implementation of Template Method pattern using FeedbackProcessor
        return new FeedbackProcessor(feedbackRepository) {
            private String oldStatus;

            @Override
            protected void applyBusinessLogic(Feedback feedback) {
                this.oldStatus = feedback.getStatus().name();
                feedback.setStatus(newStatus);

                if (newStatus == FeedbackStatus.RESOLVED || newStatus == FeedbackStatus.DISMISSED) {
                    feedback.setResolvedAt(DateTimeProvider.getInstance().now());
                }
            }

            @Override
            protected void onSuccess(Feedback updated) {
                // Notify observers about status change using the Hook method
                notifyObservers(new FeedbackEvent(
                        "STATUS_CHANGED_" + newStatus.name(),
                        updated.getId(),
                        updated.getCategory().name(),
                        updated.getMessage(),
                        updated.getIpAddress(),
                        oldStatus,
                        newStatus.name(),
                        DateTimeProvider.getInstance().now()
                ));
            }
        }.execute(feedbackId);
    }

    @Override
    public int[] getRateLimitInfo(String ipAddress) {
        int currentCount = 0;
        if (ipAddress != null && !ipAddress.isBlank()) {
            LocalDateTime windowStart = DateTimeProvider.getInstance().now().minusHours(windowHours);
            currentCount = (int) feedbackRepository.countByIpAddressSince(ipAddress, windowStart);
        }
        return new int[] { currentCount, maxRequestsPerWindow, windowHours };
    }

    /**
     * Sanitize message content to prevent XSS.
     * Removes ALL HTML tags to prevent script injection via any vector
     * (script tags, event handlers, data URIs, SVG payloads, etc.)
     */
    private String sanitizeMessage(String message) {
        if (message == null) return null;
        return message
                .replaceAll("<[^>]*>", "") // Remove all HTML tags
                .replaceAll("(?i)javascript:", "") // Remove javascript: URLs
                .replaceAll("(?i)data:", "") // Remove data: URLs
                .replaceAll("(?i)vbscript:", "") // Remove vbscript: URLs
                .trim();
    }

    // Observer pattern implementation

    @Override
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            log.debug("Observer attached: {}", observer.getClass().getSimpleName());
        }
    }

    @Override
    public void detach(Observer observer) {
        if (observers.remove(observer)) {
            log.debug("Observer detached: {}", observer.getClass().getSimpleName());
        }
    }

    @Override
    public void notifyObservers(Object event) {
        for (Observer observer : observers) {
            observer.update(this, event);
        }
    }

}
