package com.pharmacy.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler - Centralized Exception Handling.
 *
 * @ControllerAdvice intercepts exceptions thrown by any controller
 * and allows us to return consistent, structured error responses.
 *
 * Without this, Spring would return its default (less readable) error format.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // ─────────────────────────────────────────────────────────────
    //  Handle ResourceNotFoundException → HTTP 404
    // ─────────────────────────────────────────────────────────────

    /**
     * Handles cases where a medicine ID does not exist in the database.
     * Returns a structured JSON error response with HTTP 404.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex) {

        Map<String, Object> errorResponse = buildErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Resource Not Found",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // ─────────────────────────────────────────────────────────────
    //  Handle Validation Errors → HTTP 400
    // ─────────────────────────────────────────────────────────────

    /**
     * Handles @Valid annotation failures (e.g., blank name, null price).
     * Collects ALL field-level validation errors and returns them as a map.
     *
     * Example response:
     * {
     *   "status": 400,
     *   "error": "Validation Failed",
     *   "details": {
     *     "medicineName": "Medicine name is mandatory",
     *     "price": "Price is mandatory"
     *   }
     * }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        // Collect all field validation errors into a map
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", "Validation Failed");
        errorResponse.put("details", fieldErrors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // ─────────────────────────────────────────────────────────────
    //  Handle Generic Exceptions → HTTP 500
    // ─────────────────────────────────────────────────────────────

    /**
     * Catch-all handler for any unexpected exceptions.
     * Returns HTTP 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> errorResponse = buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred: " + ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ─────────────────────────────────────────────────────────────
    //  Helper: Build a consistent error response map
    // ─────────────────────────────────────────────────────────────

    private Map<String, Object> buildErrorResponse(int status, String error, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", status);
        response.put("error", error);
        response.put("message", message);
        return response;
    }
}
