package com.bezkoder.spring.inventory.dto.response;

import com.bezkoder.spring.security.jwt.payload.response.BusinessResponseDto;

public record ItemTypeResponseDto(
        Long id,
        String name,
        String description,
        BusinessResponseDto businessResponseDto
) {}
