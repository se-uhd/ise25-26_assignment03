package de.seuhd.campuscoffee.api.exceptions;

import de.seuhd.campuscoffee.domain.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * Global exception handler for all controllers.
 * Provides centralized exception handling and standardized error responses.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles all "Not Found" exceptions from the domain layer.
     * Returns HTTP 404 (Not Found).
     *
     * @param exception theNotFoundException that was thrown
     * @param request the web request
     * @return ResponseEntity with ErrorResponse and HTTP 404
     */
    @ExceptionHandler({
            PosNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(
            RuntimeException exception,
            WebRequest request
    ) {
        log.warn("Resource not found: {}", exception.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .message(exception.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .statusMessage(HttpStatus.NOT_FOUND.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .path(extractPath(request))
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handles duplicate/uniqueness constraint violations.
     * Returns HTTP 409 (Conflict) - standard status for resource conflicts.
     *
     * @param exception the duplicate exception that was thrown
     * @param request the web request
     * @return ResponseEntity with ErrorResponse and HTTP 409
     */
    @ExceptionHandler({
            DuplicatePosNameException.class
    })
    public ResponseEntity<ErrorResponse> handleDuplicateException(
            RuntimeException exception,
            WebRequest request
    ) {
        log.warn("Duplicate resource: {}", exception.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .message(exception.getMessage())
                .statusCode(HttpStatus.CONFLICT.value())
                .statusMessage(HttpStatus.CONFLICT.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .path(extractPath(request))
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Handles IllegalArgumentException (e.g., ID mismatch).
     * Returns HTTP 400 (Bad Request).
     *
     * @param exception the IllegalArgumentException that was thrown
     * @param request the web request
     * @return ResponseEntity with ErrorResponse and HTTP 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException exception,
            WebRequest request
    ) {
        log.warn("Invalid argument: {}", exception.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .message(exception.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .statusMessage(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .path(extractPath(request))
                .build();

        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Fallback handler for unexpected exceptions.
     * Returns HTTP 500 (Internal Server Error).
     *
     * @param exception the unexpected exception that was thrown
     * @param request the web request
     * @return ResponseEntity with ErrorResponse and HTTP 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception exception,
            WebRequest request
    ) {
        log.error("Unexpected error occurred", exception);

        ErrorResponse error = ErrorResponse.builder()
                .message("An unexpected error occurred.")
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .statusMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .path(extractPath(request))
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Extracts the request path from the WebRequest.
     *
     * @param request the web request
     * @return the request URI or "unknown" if not available
     */
    private String extractPath(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
            return servletRequest.getRequestURI();
        }
        return "unknown";
    }
}
