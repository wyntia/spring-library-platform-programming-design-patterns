package org.pollub.feedback.service;

import lombok.RequiredArgsConstructor;
import org.pollub.feedback.model.Feedback;
import org.pollub.feedback.model.FeedbackStatus;
import org.pollub.feedback.report.FeedbackTextReportFormatter;
import org.pollub.feedback.report.FeedbackTextReportOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//Lab9 : Refaktor text-digest Start
@Service
@RequiredArgsConstructor
public class FeedbackTextDigestService implements IFeedbackTextDigestService {

    private final IFeedbackService feedbackService;
    private final FeedbackTextReportFormatter textReportFormatter;

    @Override
    @Transactional(readOnly = true)
    public String buildAdminTextDigest(FeedbackStatus status, int maxItems) {
        int capped = clampMaxItems(maxItems);
        List<Feedback> items = loadFeedbacks(status);
        FeedbackTextReportOptions options = new FeedbackTextReportOptions(
                capped,
                true,
                "---",
                200,
                true
        );
        return textReportFormatter.format(items, options);
    }

    private static int clampMaxItems(int maxItems) {
        int n = maxItems;
        if (n <= 0) {
            n = 50;
        }
        if (n > 500) {
            n = 500;
        }
        return n;
    }

    private List<Feedback> loadFeedbacks(FeedbackStatus statusOrNull) {
        if (statusOrNull != null) {
            return feedbackService.getFeedbacksByStatus(statusOrNull);
        }
        return feedbackService.getAllFeedbacks();
    }
}
//Lab9 : Refaktor text-digest Stop
