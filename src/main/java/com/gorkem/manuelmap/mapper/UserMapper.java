package com.gorkem.manuelmap.mapper;

import com.gorkem.manuelmap.dto.request.UserRequestDto;
import com.gorkem.manuelmap.dto.response.UserResponseDto;
import com.gorkem.manuelmap.entity.Company;
import com.gorkem.manuelmap.entity.User;

public interface UserMapper {
     User toEntity(UserRequestDto userRequestDto);
     UserResponseDto toResponseDto(User user);
}