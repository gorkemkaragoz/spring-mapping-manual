package com.gorkem.manuelmap.serviceImpl;


import com.gorkem.manuelmap.dto.request.CompanyRequestDto;
import com.gorkem.manuelmap.dto.response.CompanyResponseDto;
import com.gorkem.manuelmap.entity.Company;
import com.gorkem.manuelmap.mapper.CompanyMapper;
import com.gorkem.manuelmap.repo.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyMapper companyMapper;

    @InjectMocks
    private CompanyServiceImpl companyService;

    private Company firstCompany;
    private Company secondCompany;

    @BeforeEach
    void setUp(){
        firstCompany = Company.builder()
                .id(1L)
                .name("Turkcell")
                .industry("Telecommunication")
                .city("Istanbul")
                .build();

        secondCompany = Company.builder()
                .id(2L)
                .name("Havelsan")
                .industry("Defender Industry")
                .city("Ankara")
                .build();
    }


    // getAllCompanies()

    @Test
    void getAllCompanies_shouldReturnAllCompanies(){
        // Given
        List<Company> expectedCompanyList = Arrays.asList(firstCompany,secondCompany);
        CompanyResponseDto firstDto = new CompanyResponseDto
                (null,"Turkcell", "Telecommunication", "Istanbul", List.of());
        CompanyResponseDto secondDto = new CompanyResponseDto
                (null, "Havelsan", "Defender Industry", "Ankara", List.of());


        when(companyRepository.findAll()).thenReturn(expectedCompanyList);
        when(companyMapper.toResponseDto(firstCompany)).thenReturn(firstDto);
        when(companyMapper.toResponseDto(secondCompany)).thenReturn(secondDto);

        // When
        List<CompanyResponseDto> result = companyService.getAllCompanies();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(firstDto, result.get(0));
        assertEquals(secondDto, result.get(1));
        verify(companyRepository, times(1)).findAll();
        verify(companyMapper, times(1)).toResponseDto(firstCompany);
        verify(companyMapper, times(1)).toResponseDto(secondCompany);
    }

    @Test
    void getAllCompanies_whenNoCompanyExist_shouldReturnEmptyList(){
        // Given
        when(companyRepository.findAll()).thenReturn(List.of());

        // When
        List<CompanyResponseDto> result = companyService.getAllCompanies();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(companyRepository, times(1)).findAll();
    }


    // getOneCompany()

    @Test
    void getOneCompany_whenCompanyExist_shouldReturnCompany(){
        // Given
        CompanyResponseDto expectedDto = new CompanyResponseDto
                (1L, "Turkcell","Telecommunication", "Istanbul", List.of());

        when(companyRepository.findById(1L)).thenReturn(Optional.of(firstCompany));
        when(companyMapper.toResponseDto(firstCompany)).thenReturn(expectedDto);


        // When
        CompanyResponseDto result = companyService.getOneCompany(1L);

        // Then
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(companyRepository, times(1)).findById(1L);
        verify(companyMapper, times(1)).toResponseDto(firstCompany);
    }

    @Test
    void getOneCompany_whenCompanyNotFound_shouldThrowException(){
        // Given
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> companyService.getOneCompany(1L));
        verify(companyRepository, times(1)).findById(1L);
    }

    // createCompany()

    @Test
    void createCompany_whenCompanyExist_shouldSaveAndReturnCompany(){
        // Given
        CompanyRequestDto requestDto = new CompanyRequestDto
                ("Baykar","Defender Industry","Istanbul");

        Company companyToSave = Company.builder()
                .name("Baykar").industry("Defender Industry").city("Istanbul").build();
        Company savedCompany = Company.builder()
                .id(3L).name("Baykar").industry("Defender Industry").city("Istanbul").build();

        CompanyResponseDto expectedDto = new CompanyResponseDto
                (3L,"Baykar","Defender Industry","Istnabul",List.of());

        when(companyMapper.toEntity(requestDto)).thenReturn(companyToSave);
        when(companyRepository.save(any(Company.class))).thenReturn(savedCompany);
        when(companyMapper.toResponseDto(savedCompany)).thenReturn(expectedDto);

        // When
        CompanyResponseDto result = companyService.createCompany(requestDto);

        // Then
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(companyMapper,times(1)).toEntity(requestDto);
        verify(companyRepository, times(1)).save(companyToSave);
        verify(companyMapper, times(1)).toResponseDto(savedCompany);
    }


    // updateCompany()

    @Test
    void updateCompany_whenCompanyExist_shouldUpdateAndReturnCompany(){
        // Given
        Long companyId = 1L;
        CompanyRequestDto requestDto = new CompanyRequestDto("Turkcell Updated", "Telecommunication", "Ankara");

        Company updatedCompany = Company.builder()
                .id(1L).name("Turkcell Updated").industry("Telecommunication").city("Ankara").build();

        CompanyResponseDto expectedDto = new CompanyResponseDto(1L, "Turkcell Updated", "Telecommunication", "Ankara", List.of());

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(firstCompany));
        when(companyRepository.save(firstCompany)).thenReturn(updatedCompany);
        when(companyMapper.toResponseDto(updatedCompany)).thenReturn(expectedDto);

        // When
        CompanyResponseDto result = companyService.updateCompany(companyId, requestDto);

        // Then
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(companyRepository, times(1)).findById(companyId);
        verify(companyRepository, times(1)).save(firstCompany);
        verify(companyMapper, times(1)).toResponseDto(updatedCompany);
    }

    @Test
    void updateCompany_whenCompanyNotFound_shouldThrowException(){
        // Given
        Long companyId = 99L;
        CompanyRequestDto requestDto = new CompanyRequestDto("Turkcell", "Telecommunication", "Istanbul");

        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> companyService.updateCompany(companyId, requestDto));
        verify(companyRepository, times(1)).findById(companyId);
        verify(companyRepository, never()).save(any());
    }


    // deleteOneCompany()

    @Test
    void deleteOneCompany_whenCompanyExist_shouldDeleteCompany(){
        // Given
        Long companyIdToDelete = 1L;
        when(companyRepository.existsById(companyIdToDelete)).thenReturn(true);

        // When
        companyService.deleteOneCompany(companyIdToDelete);

        // Then
        verify(companyRepository, times(1)).existsById(companyIdToDelete);
        verify(companyRepository, times(1)).deleteById(companyIdToDelete);
    }

}
