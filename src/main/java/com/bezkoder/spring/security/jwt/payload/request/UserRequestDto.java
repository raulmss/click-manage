package com.bezkoder.spring.security.jwt.payload.request;

import com.bezkoder.spring.inventory.dto.request.BusinessRequestDto;

import java.util.Set;

public record UserRequestDto(
        String username,
        String email,
        Set<RoleRequestDto> roles,
        BusinessRequestDto businessResponseDto
) {
}
