package com.gorkem.manuelmap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gorkem.manuelmap.dto.request.CompanyRequestDto;
import com.gorkem.manuelmap.dto.response.CompanyResponseDto;
import com.gorkem.manuelmap.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompanyController.class)
public class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CompanyService companyService;

    private CompanyResponseDto firstDto;
    private CompanyResponseDto secondDto;

    @BeforeEach
    void setUp() {
        firstDto = new CompanyResponseDto
                (1L, "Turkcell", "Telecommunication", "Istanbul", List.of());
        secondDto = new CompanyResponseDto
                (2L, "Havelsan", "Defender Industry", "Ankara", List.of());
    }

    // getAll()
    @Test
    void getAll_shouldReturnCompanies() throws Exception{
        // Given
        when(companyService.getAllCompanies()).thenReturn(Arrays.asList(firstDto,secondDto));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/companies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Turkcell"))
                .andExpect(jsonPath("$[1].name").value("Havelsan"));
    }

    // getOne()
    @Test
    void getOne_whenCompanyExist_shouldReturnCompany() throws Exception {
        // Given
        when(companyService.getOneCompany(1L)).thenReturn(firstDto);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/companies/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Turkcell"))
                .andExpect(jsonPath("$.industry").value("Telecommunication"))
                .andExpect(jsonPath("$.city").value("Istanbul"));
    }

    // create()
    @Test
    void create_shouldSaveAndReturnCompany() throws Exception{
        // Given
        CompanyRequestDto requestDto = new CompanyRequestDto
                ("Turkcell","Telecommunication","Istanbul");

        when(companyService.createCompany(requestDto)).thenReturn(firstDto);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    // update()
    @Test
    void update_whenCompanyExist_shouldUpdateAndReturnCompany() throws Exception{
        // Given
        CompanyRequestDto requestDto = new CompanyRequestDto
                ("Turkcell updated","Telecommunication","Istanbul");

        when(companyService.updateCompany(1L,requestDto)).thenReturn(firstDto);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/companies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    // delete()
    @Test
    void delete_whenCompanyExist_shouldDeleteCompany() throws Exception{
        // Given
        doNothing().when(companyService).deleteOneCompany(1L);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/companies/1"))
                .andExpect(status().isOk());
    }


}
