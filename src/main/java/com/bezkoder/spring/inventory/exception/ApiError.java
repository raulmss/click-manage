package com.bezkoder.spring.inventory.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
public class ApiError {
    private final Instant timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    public ApiError(HttpStatus status, String message, String path) {
        this.timestamp = Instant.now();
        this.status = status.value(); // Extract the int value
        this.error = status.getReasonPhrase(); // "Not Found", "Conflict", etc.
        this.message = message;
        this.path = path;
    }

}

