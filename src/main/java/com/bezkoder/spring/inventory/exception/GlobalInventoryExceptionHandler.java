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
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SupplierAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleSupplierAlreadyExists(SupplierAlreadyExistsException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SupplierNotFoundException.class)
    public ResponseEntity<ApiError> handleSupplierNotFound(SupplierNotFoundException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<ApiError> handleInvalidQuantity(InvalidQuantityException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StockAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleStockAlreadyExists(StockAlreadyExistsException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiError> handleInsufficientStock(InsufficientStockException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

}
