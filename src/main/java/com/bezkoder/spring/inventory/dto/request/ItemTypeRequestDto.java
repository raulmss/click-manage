package com.bezkoder.spring.inventory.dto.request;

import com.bezkoder.spring.security.jwt.payload.request.BusinessRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ItemTypeRequestDto(

        @NotBlank(message = "Item type name is required.")
        @Size(max = 100, message = "Item type name must be at most 100 characters.")
        String name,

        @NotBlank(message = "Description is required.")
        @Size(max = 255, message = "Description must be at most 255 characters.")
        String description,

        @NotNull(message = "Business information is required.")
        @Valid
        BusinessRequestDto business

) {}
