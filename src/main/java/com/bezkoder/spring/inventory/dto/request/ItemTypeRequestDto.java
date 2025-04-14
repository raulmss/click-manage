package com.bezkoder.spring.inventory.dto.request;

import com.bezkoder.spring.security.jwt.payload.request.BusinessRequestDto;

public record ItemTypeRequestDto(
        String name,
        String description,
        BusinessRequestDto businessRequestDto
) {}
