package com.bezkoder.spring.inventory.dto.response;

public record StockResponseDto(
        Long id,
        ItemResponseDto item,
        int quantity,
        Integer minThreshold
) {}
