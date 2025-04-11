package com.bezkoder.spring.inventory.dto.response;

public record ItemResponseDto(
        Long id,
        String name,
        String description,
        String typeDescription
) {}
