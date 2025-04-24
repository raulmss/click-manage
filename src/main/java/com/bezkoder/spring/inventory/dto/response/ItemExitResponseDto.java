package com.bezkoder.spring.inventory.dto.response;

import java.time.Instant;

public record ItemExitResponseDto(
        Long id,
        ItemResponseDto item,
        int quantity,
        String lotNumber,
        String reason,
        Instant exitDate,
        UserSummaryDto user
) {}
