package com.bezkoder.spring.inventory.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ItemRequestDto(

        @NotBlank(message = "Item name is required.")
        @Size(max = 100, message = "Item name must be at most 100 characters.")
        String name,

        @Size(max = 255, message = "Description must be at most 255 characters.")
        String description,

        @NotBlank(message = "Bar code is required.")
        @Size(max = 50, message = "Bar code must be at most 50 characters.")
        String barCode,

        @NotNull(message = "Item type is required.")
        @Valid
        ItemTypeRequestDto type

) {}
