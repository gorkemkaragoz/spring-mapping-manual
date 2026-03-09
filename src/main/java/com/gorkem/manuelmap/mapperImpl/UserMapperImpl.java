package com.gorkem.manuelmap.mapperImpl;

import com.gorkem.manuelmap.dto.request.UserRequestDto;
import com.gorkem.manuelmap.dto.response.UserResponseDto;
import com.gorkem.manuelmap.entity.Company;
import com.gorkem.manuelmap.entity.User;
import com.gorkem.manuelmap.mapper.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserRequestDto userRequestDto) {
        User user = new User();
        user.setUsername(userRequestDto.username());
        user.setFirstName(userRequestDto.firstName());
        user.setLastName(userRequestDto.lastName());
        user.setEmail(userRequestDto.email());
        user.setPassword(userRequestDto.password());
        user.setAge(userRequestDto.age());
        user.setSalary(userRequestDto.salary());
        return user;
    }

    @Override
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

