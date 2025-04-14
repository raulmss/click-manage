package com.bezkoder.spring.security.jwt.payload.request;

import java.util.Set;

public record UserRequestDto(
        String username,
        String email,
        Set<RoleRequestDto> roles
) {
}
