package com.bezkoder.spring.inventory.dto.request;

import java.time.LocalDate;

public record ItemRequestDto(
        String name,
        String description,
        String barCode,
        ItemTypeRequestDto type
) {}
