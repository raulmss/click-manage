package com.bezkoder.spring.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.bezkoder.spring.inventory")
public class GlobalInventoryExceptionHandler {

    @ExceptionHandler(ItemTypeAlreadyExistsException.class)
    public ResponseEntity<String> handleItemTypeAlreadyExists(ItemTypeAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(ItemAlreadyExistsException.class)
    public ResponseEntity<String> handleItemExists(ItemAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    // Optionally: Add a catch-all for other inventory-related runtime exceptions
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<String> handleOtherInventoryExceptions(RuntimeException ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal inventory error occurred.");
//    }
}
