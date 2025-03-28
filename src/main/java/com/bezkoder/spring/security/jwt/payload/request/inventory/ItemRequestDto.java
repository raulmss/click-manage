package com.bezkoder.spring.security.jwt.payload.request.inventory;

import lombok.Data;

@Data
public class ItemRequestDto {
    private String name;
    private String description;
    private Long typeId;
}
