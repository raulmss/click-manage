package com.bezkoder.spring.inventory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ItemEntryRequestDto(

        @NotNull(message = "Item ID is required.")
        Long itemId,

        // Nullable, no annotation needed
        Long supplierId,

        @Min(value = 1, message = "Quantity must be greater than zero.")
        int quantity,

        // Optional, but you can limit size if desired
        String lotNumber,

        // Optional field
        LocalDate expiryDate

) {}
