package com.bezkoder.spring.inventory.dto.request;

public record ItemRequestDto(
        String name,
        String description,
        Long typeId
) {}
