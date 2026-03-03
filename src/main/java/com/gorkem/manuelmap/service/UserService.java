package com.gorkem.manuelmap.service;

import com.gorkem.manuelmap.dto.request.UserRequestDto;
import com.gorkem.manuelmap.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {
     List<UserResponseDto> getAllUser();
     UserResponseDto getOneUser(Long userId);
     UserResponseDto createUser(UserRequestDto userRequestDto);
     UserResponseDto updateUser(Long userId, UserRequestDto userRequestDto);
     void deleteOneUser(Long userId);
}
