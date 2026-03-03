package com.gorkem.manuelmap.mapper;

import com.gorkem.manuelmap.dto.response.CompanyResponseDto;
import com.gorkem.manuelmap.dto.request.CompanyRequestDto;
import com.gorkem.manuelmap.dto.response.UserResponseDto;
import com.gorkem.manuelmap.entity.Company;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyMapper {

    public Company toEntity(CompanyRequestDto companyRequestDto) {
        Company company = new Company();
        company.setName(companyRequestDto.name());
        company.setIndustry(companyRequestDto.industry());
        company.setCity(companyRequestDto.city());
        return company;
    }

    public CompanyResponseDto toResponseDto(Company company) {
        List<UserResponseDto> userResponseDtos;

        if (company.getUsers() != null) {
            userResponseDtos = company.getUsers().stream()
                    .map(user -> new UserResponseDto(
                            user.getId(),
                            user.getUsername(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail(),
                            user.getAge(),
                            user.getSalary(),
                            company.getName()
                    )).toList();
        } else {
            userResponseDtos = List.of();
        }

        return new CompanyResponseDto(
                company.getId(),
                company.getName(),
                company.getIndustry(),
                company.getCity(),
                userResponseDtos
        );
    }
}