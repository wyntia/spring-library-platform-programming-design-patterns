package org.pollub.feedback.service;

import org.pollub.feedback.model.FeedbackStatus;

public interface IFeedbackTextDigestService {

    String buildAdminTextDigest(FeedbackStatus status, int maxItems);
}
