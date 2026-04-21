package org.pollub.feedback.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "feedback.notification")
//L4 - OCP 2 Start
public class FeedbackNotificationDataDrivenProperties {

    private List<String> channels = List.of("email", "sms");
    private List<String> emailRecipients = List.of("user@example.com");
    private List<String> smsRecipients = List.of("123-456-789");
    private String header = "[Biblioteka Miejska]";
    private String footer = "Dziękujemy za korzystanie z naszych usług!";
    private boolean loggingEnabled = true;

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    public List<String> getEmailRecipients() {
        return emailRecipients;
    }

    public void setEmailRecipients(List<String> emailRecipients) {
        this.emailRecipients = emailRecipients;
    }

    public List<String> getSmsRecipients() {
        return smsRecipients;
    }

    public void setSmsRecipients(List<String> smsRecipients) {
        this.smsRecipients = smsRecipients;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    public void setLoggingEnabled(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }
}
//L4 - OCP 2 END
