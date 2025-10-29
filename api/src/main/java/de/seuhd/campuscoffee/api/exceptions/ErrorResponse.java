package de.seuhd.campuscoffee.api.exceptions;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Standardized error response structure for all API exceptions.
 * Provides consistent error information to API consumers.
 */
@Data
@Builder
public class ErrorResponse {
    /**
     * Human-readable error message.
     */
    private String message;

    /**
     * HTTP status code (e.g., 400, 404, 500).
     */
    private int statusCode;

    /**
     * HTTP status message (e.g., "Bad Request", "Not Found").
     */
    private String statusMessage;

    /**
     * Timestamp when the error occurred.
     */
    private LocalDateTime timestamp;

    /**
     * Request path that caused the error.
     */
    private String path;
}
