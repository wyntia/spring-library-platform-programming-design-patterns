package org.pollub.feedback.report;

import org.pollub.common.config.DateTimeProvider;
import org.pollub.feedback.model.Feedback;
import org.springframework.stereotype.Component;

import java.util.List;

//Lab9 : Refaktor text-digest Start
@Component
public class FeedbackTextReportFormatter {

    public String format(List<Feedback> items, FeedbackTextReportOptions options) {
        StringBuilder out = new StringBuilder();
        if (options.addHeaderLine()) {
            out.append("INGRESS_SNAPSHOT ");
            out.append(DateTimeProvider.getInstance().now());
            out.append(System.lineSeparator());
        }

        int max = options.maxEntries();
        String separator = options.separator();
        int truncateLen = options.truncateLen();
        boolean includeUrl = options.includePageUrl();

        int idx = 0;
        for (Feedback f : items) {
            if (idx >= max) {
                break;
            }
            if (idx > 0 && separator != null && !separator.isEmpty()) {
                out.append(separator);
                out.append(System.lineSeparator());
            }

            out.append("#");
            out.append(f.getId());
            out.append(" | ");
            out.append(f.getStatus());
            out.append(" | ");
            out.append(f.getCategory());
            out.append(System.lineSeparator());

            String msg = f.getMessage();
            if (msg != null && truncateLen > 0 && msg.length() > truncateLen) {
                msg = msg.substring(0, truncateLen) + "…";
            }
            out.append(msg != null ? msg : "");
            out.append(System.lineSeparator());

            if (includeUrl) {
                String u = f.getPageUrl();
                out.append(u != null ? u : "");
                out.append(System.lineSeparator());
            }

            out.append(f.getCreatedAt() != null ? f.getCreatedAt().toString() : "");
            out.append(System.lineSeparator());

            idx++;
        }

        return out.toString();
    }
}
//Lab9 : Refaktor text-digest Stop
