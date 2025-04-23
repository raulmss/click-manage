package com.bezkoder.spring.inventory.dto.response;

public record SupplierResponseDto(
        Long id,
        String name,
        String email,
        String phone,
        String taxId,
        AddressResponseDto address,
        String contactPerson
) {}
