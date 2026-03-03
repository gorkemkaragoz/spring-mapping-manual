package com.gorkem.manuelmap.controller;

import com.gorkem.manuelmap.dto.request.UserRequestDto;
import com.gorkem.manuelmap.dto.response.UserResponseDto;
import com.gorkem.manuelmap.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponseDto> getAll() {
        return userService.getAllUser();
    }

    @GetMapping("/{id}")
    public UserResponseDto getOne(@PathVariable("id") Long userId) {
        return userService.getOneUser(userId);
    }

    @PostMapping
    public UserResponseDto create(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @PutMapping("/{id}")
    public UserResponseDto update(@PathVariable("id") Long userId, @RequestBody UserRequestDto userRequestDto) {
        return userService.updateUser(userId, userRequestDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long userId) {
        userService.deleteOneUser(userId);
    }
}