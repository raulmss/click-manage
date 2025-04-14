package com.bezkoder.spring.security.jwt.payload.request;

import com.bezkoder.spring.inventory.dto.request.AddressRequestDto;
import com.bezkoder.spring.security.jwt.payload.request.UserRequestDto;

import java.util.List;

public record BusinessRequestDto(
        String name,
        String industry,
        List<UserRequestDto> usersRequestDtoList,
        AddressRequestDto addressRequestDto
) {
}

