package org.pollub.feedback.report;

public record FeedbackTextReportOptions(
        int maxEntries,
        boolean addHeaderLine,
        String separator,
        int truncateLen,
        boolean includePageUrl
) {}
