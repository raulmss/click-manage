package com.bezkoder.spring.inventory.dto.response;

public record ItemTypeResponseDto(
        Long id,
        String name,
        String description,
        BusinessResponseDto businessResponseDto
) {}
