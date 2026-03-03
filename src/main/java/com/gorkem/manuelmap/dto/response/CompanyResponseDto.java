package com.gorkem.manuelmap.dto.response;

import java.util.List;

public record CompanyResponseDto(
        Long id,
        String name,
        String industry,
        String city,
        List<UserResponseDto> users
) {}
