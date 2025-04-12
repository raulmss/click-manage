package com.bezkoder.spring.inventory.dto.request;

public record ItemTypeRequestDto(
        String name,
        String description,
        BusinessRequestDto businessRequestDto
) {}
