package com.bezkoder.spring.security.jwt.payload.request;

public record BusinessFilterDto(
        String name,
        String industry,
        String city,
        String state
) {}
