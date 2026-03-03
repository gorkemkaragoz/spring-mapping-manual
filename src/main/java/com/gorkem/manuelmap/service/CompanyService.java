package com.gorkem.manuelmap.service;

import com.gorkem.manuelmap.dto.request.CompanyRequestDto;
import com.gorkem.manuelmap.dto.response.CompanyResponseDto;

import java.util.List;

public interface CompanyService {
    List<CompanyResponseDto> getAllCompanies();
    CompanyResponseDto getOneCompany(Long companyId);
    CompanyResponseDto createCompany(CompanyRequestDto companyRequestDto);
    CompanyResponseDto updateCompany(Long companyId, CompanyRequestDto companyRequestDto);
    void deleteOneCompany(Long companyId);

}
