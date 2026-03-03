package com.gorkem.manuelmap.controller;

import com.gorkem.manuelmap.dto.request.CompanyRequestDto;
import com.gorkem.manuelmap.dto.response.CompanyResponseDto;
import com.gorkem.manuelmap.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public List<CompanyResponseDto> getAll() {
        return companyService.getAllCompanies();
    }

    @GetMapping("/{id}")
    public CompanyResponseDto getOne(@PathVariable("id") Long companyId) {
        return companyService.getOneCompany(companyId);
    }

    @PostMapping
    public CompanyResponseDto create(@RequestBody CompanyRequestDto companyRequestDto) {
        return companyService.createCompany(companyRequestDto);
    }

    @PutMapping("/{id}")
    public CompanyResponseDto update(@PathVariable("id") Long companyId, @RequestBody CompanyRequestDto companyRequestDto) {
        return companyService.updateCompany(companyId, companyRequestDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long companyId) {
        companyService.deleteOneCompany(companyId);
    }
}