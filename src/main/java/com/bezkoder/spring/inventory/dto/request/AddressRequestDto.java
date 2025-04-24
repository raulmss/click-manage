package com.bezkoder.spring.inventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequestDto(
        @NotBlank(message = "Street is required.")
        @Size(max = 100, message = "Street must be at most 100 characters.")
        String street,

        @NotBlank(message = "City is required.")
        @Size(max = 50, message = "City must be at most 50 characters.")
        String city,

        @NotBlank(message = "State is required.")
        @Size(max = 50, message = "State must be at most 50 characters.")
        String state,

        @NotBlank(message = "Country is required.")
        @Size(max = 50, message = "Country must be at most 50 characters.")
        String country,

        @NotBlank(message = "Postal code is required.")
        @Size(max = 20, message = "Postal code must be at most 20 characters.")
        String postalCode
) {}
