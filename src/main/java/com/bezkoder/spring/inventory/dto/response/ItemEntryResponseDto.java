package com.bezkoder.spring.inventory.dto.response;

import java.time.Instant;
import java.time.LocalDate;

public record ItemEntryResponseDto(
        Long id,
        ItemResponseDto item,
        SupplierResponseDto supplier,
        UserSummaryDto user,
        int quantity,
        String lotNumber,
        LocalDate expiryDate,
        Instant entryDate
) {}
