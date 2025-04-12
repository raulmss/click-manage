package com.bezkoder.spring.security.jwt.payload.response;

import com.bezkoder.spring.inventory.dto.response.BusinessResponseDto;

import java.util.Set;

public record UserResponseDto(
        Long id,
        String username,
        String email,
        Set<RoleResponseDto> roles,
        BusinessResponseDto businessResponseDto
) {
}
