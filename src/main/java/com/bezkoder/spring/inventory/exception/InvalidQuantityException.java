package com.bezkoder.spring.inventory.exception;

public class InvalidQuantityException extends RuntimeException {

    public InvalidQuantityException(String message) {
        super(message);
    }
}
