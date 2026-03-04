package org.pollub.common.exception;

import java.time.LocalDateTime;
import org.pollub.common.config.DateTimeProvider;
import java.util.HashMap;
import java.util.Map;

/**
 * API response object with manual Builder pattern.
 * Replaces inline Map creation in GlobalExceptionHandler.
 */
public class ApiResponse {
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final Object details;

    private ApiResponse(Builder builder) {
        this.timestamp = builder.timestamp;
        this.status = builder.status;
        this.error = builder.error;
        this.details = builder.details;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public Object getDetails() { return details; }

    /**
     * Converts to Map for JSON serialization in ResponseEntity.
     */
    public Map<String, Object> toMap() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", timestamp);
        response.put("status", status);
        response.put("error", error);
        if (details != null) {
            response.put("details", details);
        }
        return response;
    }

    // Lab2 - Builder 2 Start

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Manual Builder for ApiResponse with fluent API.
     */
    public static class Builder {
        private LocalDateTime timestamp = DateTimeProvider.getInstance().now();
        private int status;
        private String error;
        private Object details;

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder details(Object details) {
            this.details = details;
            return this;
        }

        public ApiResponse build() {
            return new ApiResponse(this);
        }
    }
    // End Builder 2

}
