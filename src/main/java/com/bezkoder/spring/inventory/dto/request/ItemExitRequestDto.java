package com.bezkoder.spring.inventory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ItemExitRequestDto(

        @NotNull(message = "Item ID is required.")
        Long itemId,

        @Min(value = 1, message = "Quantity must be greater than zero.")
        int quantity,

        // Optional, but you can add size constraints if needed
        @Size(max = 50, message = "Lot number must be at most 50 characters.")
        String lotNumber,

        @Size(max = 255, message = "Reason must be at most 255 characters.")
        String reason

) {}
