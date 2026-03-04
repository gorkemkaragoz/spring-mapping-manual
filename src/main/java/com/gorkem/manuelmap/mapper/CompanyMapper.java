package com.gorkem.manuelmap.mapper;

import com.gorkem.manuelmap.dto.response.CompanyResponseDto;
import com.gorkem.manuelmap.dto.request.CompanyRequestDto;
import com.gorkem.manuelmap.entity.Company;


public interface CompanyMapper {
     Company toEntity(CompanyRequestDto companyRequestDto);
     CompanyResponseDto toResponseDto(Company company);
}