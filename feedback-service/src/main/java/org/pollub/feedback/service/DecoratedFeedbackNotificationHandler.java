package org.pollub.feedback.service;

import notification.EmailNotification;
import notification.LoggingNotificationDecorator;
import notification.NotificationComponent;
import notification.NotificationGroup;
import notification.NotificationTemplateDecorator;
import notification.SmsNotification;
import org.pollub.feedback.model.Feedback;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
//L4 - OCP 2 Start
public class DecoratedFeedbackNotificationHandler implements FeedbackNotificationHandler {

    private final FeedbackNotificationDataDrivenProperties feedbackNotificationDataDrivenProperties;
    
    // start L6 Decorator + Composite
    private final NotificationComponent notificationSender;
    // end L6 Decorator + Composite

    public DecoratedFeedbackNotificationHandler(FeedbackNotificationDataDrivenProperties feedbackNotificationDataDrivenProperties) {
        this.feedbackNotificationDataDrivenProperties = feedbackNotificationDataDrivenProperties;
        this.notificationSender = buildNotificationSender();
    }

    private NotificationComponent buildNotificationSender() {
        NotificationGroup notificationGroup = new NotificationGroup();
        addConfiguredChannels(notificationGroup);

        NotificationComponent component = new NotificationTemplateDecorator(
                notificationGroup,
                feedbackNotificationDataDrivenProperties.getHeader(),
                feedbackNotificationDataDrivenProperties.getFooter()
        );

        if (feedbackNotificationDataDrivenProperties.isLoggingEnabled()) {
            return new LoggingNotificationDecorator(component);
        }
        return component;
    }

    private void addConfiguredChannels(NotificationGroup notificationGroup) {
        List<String> channels = feedbackNotificationDataDrivenProperties.getChannels();
        for (String channel : channels) {
            String normalizedChannel = channel.toLowerCase(Locale.ROOT);
            if ("email".equals(normalizedChannel)) {
                addEmailRecipients(notificationGroup);
            }
            if ("sms".equals(normalizedChannel)) {
                addSmsRecipients(notificationGroup);
            }
        }
    }

    private void addEmailRecipients(NotificationGroup notificationGroup) {
        for (String emailRecipient : feedbackNotificationDataDrivenProperties.getEmailRecipients()) {
            notificationGroup.add(new EmailNotification(emailRecipient));
        }
    }

    private void addSmsRecipients(NotificationGroup notificationGroup) {
        for (String smsRecipient : feedbackNotificationDataDrivenProperties.getSmsRecipients()) {
            notificationGroup.add(new SmsNotification(smsRecipient));
        }
    }

    @Override
    public void sendSubmissionNotification(Feedback feedback) {
        notificationSender.send("Nowe zgłoszenie: " + feedback.getMessage());
    }
}
//L4 - OCP 2 END
