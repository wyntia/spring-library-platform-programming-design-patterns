package org.pollub.feedback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pollub.common.config.DateTimeProvider;
import org.pollub.feedback.model.Feedback;
import org.pollub.feedback.model.FeedbackStatus;
import org.pollub.feedback.model.dto.FeedbackRequestDto;
import org.pollub.feedback.repository.IFeedbackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
//Lab5 : ISP 3 Start
public class FeedbackSubmissionService implements IFeedbackSubmissionService {
//Lab5 : ISP 3 End

    private final IFeedbackRepository feedbackRepository;
    private final FeedbackEventPublisher feedbackEventPublisher;
    private final FeedbackNotificationService feedbackNotificationService;
    //L4 - OCP 3 Start
    private final FeedbackSanitizationPolicy feedbackSanitizationPolicy;
    //L4 - OCP 3 END
    //Lab5 : DIP 3 Start
    private final org.pollub.feedback.service.moderator.IFeedbackModerator feedbackModerator;
    //Lab5 : DIP 3 End

    @Transactional
    public Feedback submitFeedback(FeedbackRequestDto dto, String ipAddress) {
        
        //Lab5 : DIP 3 Start
        if (!feedbackModerator.isAppropriate(dto.message())) {
            throw new IllegalArgumentException("Feedback zawiera niedozwolone treści i został odrzucony.");
        }
        //Lab5 : DIP 3 End

        Feedback feedback = Feedback.builder()
                .category(dto.category())
                .message(dto.message())
                .pageUrl(dto.pageUrl())
                .ipAddress(ipAddress)
                .createdAt(DateTimeProvider.getInstance().now())
                .status(FeedbackStatus.NEW)
                .build();

        //L4 - OCP 3 Start
        feedbackSanitizationPolicy.sanitize(feedback);
        //L4 - OCP 3 END

        Feedback saved = feedbackRepository.save(feedback);
        log.info("Feedback submitted: id={}, category={}", saved.getId(), saved.getCategory());

        feedbackEventPublisher.publishFeedbackSubmitted(saved);
        feedbackNotificationService.sendSubmissionNotification(saved);

        return saved;
    }
}

//SRP3 End