package com.gorkem.manuelmap.mapper;

import com.gorkem.manuelmap.dto.request.UserRequestDto;
import com.gorkem.manuelmap.dto.response.UserResponseDto;
import com.gorkem.manuelmap.entity.Company;
import com.gorkem.manuelmap.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequestDto userRequestDto, Company company) {
        User user = new User();
        user.setUsername(userRequestDto.username());
        user.setFirstName(userRequestDto.firstName());
        user.setLastName(userRequestDto.lastName());
        user.setEmail(userRequestDto.email());
        user.setPassword(userRequestDto.password());
        user.setAge(userRequestDto.age());
        user.setSalary(userRequestDto.salary());
        user.setCompany(company);
        return user;
    }

    public UserResponseDto toResponseDto(User user) {
        String companyName;

        if (user.getCompany() != null) {
            companyName = user.getCompany().getName();
        } else {
            companyName = null;
        }

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAge(),
                user.getSalary(),
                companyName
        );
    }

}