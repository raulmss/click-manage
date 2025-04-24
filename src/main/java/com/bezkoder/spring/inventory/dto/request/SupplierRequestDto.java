package com.bezkoder.spring.inventory.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SupplierRequestDto(

        @NotBlank(message = "Supplier name is required.")
        @Size(max = 100, message = "Supplier name must be at most 100 characters.")
        String name,

        @Email(message = "Email must be valid.")
        @Size(max = 100, message = "Email must be at most 100 characters.")
        String email,

        @Size(max = 20, message = "Phone number must be at most 20 characters.")
        String phone,

        @Size(max = 20, message = "Tax ID must be at most 20 characters.")
        String taxId,

        @Valid
        AddressRequestDto addressRequestDto,

        @Size(max = 100, message = "Contact person must be at most 100 characters.")
        String contactPerson

) {}
