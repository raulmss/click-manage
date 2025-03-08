package com.bezkoder.spring.security.jwt.exception;

public class BusinessNotFoundException extends RuntimeException {
    public BusinessNotFoundException(String businessName) {
        super("Error: Business '" + businessName + "' does not exist. Please contact your administrator.");
    }
}
