package com.pharmacy.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ResourceNotFoundException - Custom Runtime Exception.
 *
 * Thrown when a requested resource (e.g., Medicine by ID) does not exist.
 *
 * @ResponseStatus(HttpStatus.NOT_FOUND) tells Spring to return HTTP 404
 * when this exception is thrown (handled also by GlobalExceptionHandler).
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * @param message descriptive message about which resource was not found
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
