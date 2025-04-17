package com.bezkoder.spring.inventory.dto.response;

import java.time.LocalDate;

public record ItemResponseDto(
        Long id,
        String name,
        String description,
        String barCode,
        ItemTypeResponseDto type
) {}
