package org.pollub.feedback.service;

import org.pollub.feedback.model.FeedbackStatus;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "feedback.moderation")
//L4 - OCP 3 Start
public class FeedbackModerationDataDrivenProperties {

    private List<FeedbackStatus> resolveAtStatuses = List.of(FeedbackStatus.RESOLVED, FeedbackStatus.DISMISSED);

    public List<FeedbackStatus> getResolveAtStatuses() {
        return resolveAtStatuses;
    }

    public void setResolveAtStatuses(List<FeedbackStatus> resolveAtStatuses) {
        this.resolveAtStatuses = resolveAtStatuses;
    }
}
//L4 - OCP 3 END
