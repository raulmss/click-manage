package com.bezkoder.spring.inventory.dto.request;

public record BusinessRequestDto(
        String name,
        String industry,
        AddressRequestDto addressRequestDto
) {
}

