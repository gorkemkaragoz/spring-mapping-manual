package com.gorkem.manuelmap.dto.response;

public record UserResponseDto(
        Long id,
        String username,
        String firstName,
        String lastName,
        String email,
        Integer age,
        Integer salary,
        String companyName
) {}