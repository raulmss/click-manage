package com.bezkoder.spring.security.jwt.payload.response.inventory;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemTypeResponseDto {
    private Long id;
    private String description;
}
