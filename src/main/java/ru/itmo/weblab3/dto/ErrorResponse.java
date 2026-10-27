package ru.itmo.weblab3.dto;

import java.time.Instant;
import java.util.Map;

public class ErrorResponse {

    private final String timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final Map<String, String> details;

    public ErrorResponse(
            int status,
            String error,
            String message
    ) {
        this(status, error, message, null);
    }

    public ErrorResponse(
            int status,
            String error,
            String message,
            Map<String, String> details
    ) {
        this.timestamp = Instant.now().toString();
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getDetails() {
        return details;
    }
}