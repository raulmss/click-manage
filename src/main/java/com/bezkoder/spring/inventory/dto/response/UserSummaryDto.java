package com.bezkoder.spring.inventory.dto.response;

public record UserSummaryDto(
        Long id,
        String username,
        String email
) {}
