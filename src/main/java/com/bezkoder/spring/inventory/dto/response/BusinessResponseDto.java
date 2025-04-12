package com.bezkoder.spring.inventory.dto.response;

public record BusinessResponseDto(
        Long id,
        String name,
        String industry,
        AddressResponseDto addressResponseDto
) {
}

