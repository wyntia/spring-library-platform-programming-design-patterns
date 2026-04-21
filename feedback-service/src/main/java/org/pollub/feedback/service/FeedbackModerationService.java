package org.pollub.feedback.service;

import lombok.RequiredArgsConstructor;
import org.pollub.common.config.DateTimeProvider;
import org.pollub.feedback.interpreter.FeedbackSearchExpression;
import org.pollub.feedback.interpreter.StatusExpression;
import org.pollub.feedback.model.Feedback;
import org.pollub.feedback.model.FeedbackStatus;
import org.pollub.feedback.repository.IFeedbackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
// Lab5 : ISP 3 Start
public class FeedbackModerationService implements IFeedbackQueryService, IFeedbackStatusModerationService {
    // Lab5 : ISP 3 End

    private final IFeedbackRepository feedbackRepository;
    private final FeedbackEventPublisher feedbackEventPublisher;
    // L4 - OCP 3 Start
    private final FeedbackModerationDataDrivenProperties feedbackModerationDataDrivenProperties;
    // L4 - OCP 3 END

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Feedback> getFeedbacksByStatus(FeedbackStatus status) {
        List<Feedback> allFeedbacks = feedbackRepository.findAllByOrderByCreatedAtDesc();
        FeedbackSearchExpression expr = new StatusExpression(status);
        return expr.interpret(allFeedbacks);
    }

    @Transactional
    public Feedback updateStatus(Long feedbackId, FeedbackStatus newStatus) {
        return new FeedbackProcessor(feedbackRepository) {
            private String oldStatus;

            @Override
            protected void applyBusinessLogic(Feedback feedback) {
                this.oldStatus = feedback.getStatus().name();
                feedback.setStatus(newStatus);

                // L4 - OCP 3 Start
                if (feedbackModerationDataDrivenProperties.getResolveAtStatuses().contains(newStatus)) {
                    feedback.setResolvedAt(DateTimeProvider.getInstance().now());
                }
                // L4 - OCP 3 END
            }

            @Override
            protected void onSuccess(Feedback updated) {
                String newStatusName = newStatus != null ? newStatus.name() : "null";
                feedbackEventPublisher.publishStatusChanged(updated, oldStatus, newStatusName);
            }
        }.execute(feedbackId);
    }
}
// SRP3 End