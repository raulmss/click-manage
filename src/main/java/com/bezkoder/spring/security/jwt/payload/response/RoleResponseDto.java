package com.bezkoder.spring.security.jwt.payload.response;

import com.bezkoder.spring.security.jwt.models.ERole;

public record RoleResponseDto(
        Integer id,
        ERole name
) {
}
