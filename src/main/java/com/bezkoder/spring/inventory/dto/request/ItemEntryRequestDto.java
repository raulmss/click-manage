package com.bezkoder.spring.inventory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ItemEntryRequestDto(

        @NotNull(message = "Item ID is required.")
        Long itemId,

        // Nullable, no annotation needed
        Long supplierId,

        @Min(value = 1, message = "Quantity must be greater than zero.")
        int quantity,

        // Optional, but you can limit size if desired
        @Size(max = 50, message = "Lot number must be less than 50 characters.")
        String lotNumber,

        // Optional field
        LocalDate expiryDate

) {}
