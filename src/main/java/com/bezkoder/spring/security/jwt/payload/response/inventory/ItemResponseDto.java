package com.bezkoder.spring.security.jwt.payload.response.inventory;

import lombok.Data;

@Data
public class ItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private String typeDescription;
}
