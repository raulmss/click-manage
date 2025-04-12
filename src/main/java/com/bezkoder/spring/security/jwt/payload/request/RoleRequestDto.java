package com.bezkoder.spring.security.jwt.payload.request;

import com.bezkoder.spring.security.jwt.models.ERole;

public record RoleRequestDto(
        ERole name
) {
}
