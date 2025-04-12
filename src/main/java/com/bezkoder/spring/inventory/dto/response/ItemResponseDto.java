package com.bezkoder.spring.inventory.dto.response;

public record ItemResponseDto(
        String name,
        String description,
        ItemTypeResponseDto itemTypeResponseDto,
        BusinessResponseDto businessResponseDto
) {}
