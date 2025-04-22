package com.bezkoder.spring.inventory.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.bezkoder.spring.inventory")
public class GlobalInventoryExceptionHandler {

    @ExceptionHandler(ItemTypeAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleItemTypeAlreadyExists(ItemTypeAlreadyExistsException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ItemAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleItemExists(ItemAlreadyExistsException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleOtherInventoryExceptions(RuntimeException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "An internal inventory error occurred.", request.getRequestURI());
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
