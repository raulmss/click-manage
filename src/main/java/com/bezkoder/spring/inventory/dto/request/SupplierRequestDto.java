package com.bezkoder.spring.inventory.dto.request;

public record SupplierRequestDto(
        String name,
        String email,
        String phone,
        String taxId,
        AddressRequestDto addressRequestDto,
        String contactPerson
) {}
