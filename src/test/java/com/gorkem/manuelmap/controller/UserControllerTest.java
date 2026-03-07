package com.gorkem.manuelmap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gorkem.manuelmap.dto.request.UserRequestDto;
import com.gorkem.manuelmap.dto.response.UserResponseDto;
import com.gorkem.manuelmap.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private UserResponseDto firstDto;
    private UserResponseDto secondDto;

    @BeforeEach
    void setUp() {
        firstDto = new UserResponseDto(1L, "gorkem", "Görkem", "Karagöz",
                "gorkem@gmail.com", 22, 7500, null);
        secondDto = new UserResponseDto(2L, "ahmet", "Ahmet", "Yılmaz",
                "ahmet@gmail.com", 25, 9000, null);
    }

    // getAll();
    @Test
    void getAll_shouldReturnAllUsers() throws Exception {
        // Given
        when(userService.getAllUser()).thenReturn(Arrays.asList(firstDto,secondDto));

        //When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("gorkem"))
                .andExpect(jsonPath("$[1].username").value("ahmet"));
    }

    // getOne()
    @Test
    void getOne_whenUserExists_shouldReturnUser() throws Exception{
        // Given
        when(userService.getOneUser(1L)).thenReturn(firstDto);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("gorkem"))
                .andExpect(jsonPath("$.firstName").value("Görkem"))
                .andExpect(jsonPath("$.email").value("gorkem@gmail.com"));
    }

    // create()
    @Test
    void create_shouldSaveAndReturnUser() throws Exception{
        // Given
        UserRequestDto requestDto = new UserRequestDto(
                "gorkem", "Görkem", "Karagöz",
                "gorkem@gmail.com", "123456", 22, 7500, null);

        when(userService.createUser(requestDto)).thenReturn(firstDto);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

    }

    // update()
    @Test
    void update_whenUserExists_shouldUpdateAndReturnUser() throws Exception {
        // Given
        UserRequestDto requestDto = new UserRequestDto(
                "gorkem_updated", "Görkem", "Karagöz",
                "gorkem@gmail.com", "123456", 22, 8000, null);

        when(userService.updateUser(1L, requestDto)).thenReturn(firstDto);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    // delete()
    @Test
    void delete_whenUserExists_shouldDeleteUser() throws Exception {
        // Given
        doNothing().when(userService).deleteOneUser(1L);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(status().isOk());

    }

}
