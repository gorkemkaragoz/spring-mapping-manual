package com.gorkem.manuelmap.dto.request;

public record UserRequestDto(
        String username,
        String firstName,
        String lastName,
        String email,
        String password,
        Integer age,
        Integer salary,
        Long companyId
) {}
