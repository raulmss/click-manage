package com.bezkoder.spring.inventory.dto.response;

public record AddressResponseDto(
        Long id,
        String street,
        String city,
        String state,
        String country,
        String postalCode
) {
}
