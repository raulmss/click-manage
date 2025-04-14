package com.bezkoder.spring.security.jwt.payload.response;

import com.bezkoder.spring.inventory.dto.response.AddressResponseDto;
import com.bezkoder.spring.security.jwt.payload.response.UserResponseDto;

import java.util.List;

public record BusinessResponseDto(
        Long id,
        String name,
        String industry,
        List<UserResponseDto> userResponseDtoList,
        AddressResponseDto addressResponseDto
) {
}

