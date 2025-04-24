package com.bezkoder.spring.inventory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StockRequestDto(

        @NotNull(message = "Item ID is required")
        Long itemId,

        @Min(value = 0, message = "Minimum threshold must be zero or positive")
        Integer minThreshold // Optional, but if provided, must be >= 0
) {}
