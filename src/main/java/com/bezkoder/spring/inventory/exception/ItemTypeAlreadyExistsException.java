package com.bezkoder.spring.inventory.exception;

public class ItemTypeAlreadyExistsException extends RuntimeException {
    public ItemTypeAlreadyExistsException(String name) {
        super("Item type with name '" + name + "' already exists for this business.");
    }
}