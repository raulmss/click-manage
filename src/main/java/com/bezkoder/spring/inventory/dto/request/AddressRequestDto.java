package com.bezkoder.spring.inventory.dto.request;

public record AddressRequestDto(
        String street,
        String city,
        String state,
        String country,
        String postalCode
) {
}
