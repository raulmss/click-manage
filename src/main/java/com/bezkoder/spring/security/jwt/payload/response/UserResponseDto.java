package com.bezkoder.spring.security.jwt.payload.response;

import java.util.Set;

public record UserResponseDto(
        Long id,
        String username,
        String email,
        Set<RoleResponseDto> roles
) {
}
