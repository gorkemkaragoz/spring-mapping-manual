package com.gorkem.manuelmap.serviceImpl;

import com.gorkem.manuelmap.dto.request.CompanyRequestDto;
import com.gorkem.manuelmap.dto.response.CompanyResponseDto;
import com.gorkem.manuelmap.entity.Company;
import com.gorkem.manuelmap.mapper.CompanyMapper;
import com.gorkem.manuelmap.repo.CompanyRepository;
import com.gorkem.manuelmap.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Override
    public List<CompanyResponseDto> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .map(company -> companyMapper.toResponseDto(company))
                .toList();
    }

    @Override
    public CompanyResponseDto getOneCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));
        return companyMapper.toResponseDto(company);
    }

    @Override
    public CompanyResponseDto createCompany(CompanyRequestDto companyRequestDto) {
        Company companyToSave = companyMapper.toEntity(companyRequestDto);
        Company savedCompany = companyRepository.save(companyToSave);
        return companyMapper.toResponseDto(savedCompany);
    }

    @Override
    public CompanyResponseDto updateCompany(Long companyId, CompanyRequestDto companyRequestDto) {
        Company existingCompany = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));

        existingCompany.setName(companyRequestDto.name());
        existingCompany.setIndustry(companyRequestDto.industry());
        existingCompany.setCity(companyRequestDto.city());

        Company updatedCompany = companyRepository.save(existingCompany);
        return companyMapper.toResponseDto(updatedCompany);
    }

    @Override
    public void deleteOneCompany(Long companyId) {
        if (!companyRepository.existsById(companyId)) {
            throw new RuntimeException("Company not found with id: " + companyId);
        }
        companyRepository.deleteById(companyId);
    }
}