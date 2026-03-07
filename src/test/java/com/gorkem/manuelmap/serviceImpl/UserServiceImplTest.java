package com.gorkem.manuelmap.serviceImpl;


import com.gorkem.manuelmap.dto.request.UserRequestDto;
import com.gorkem.manuelmap.dto.response.UserResponseDto;
import com.gorkem.manuelmap.entity.Company;
import com.gorkem.manuelmap.entity.User;
import com.gorkem.manuelmap.mapper.UserMapper;
import com.gorkem.manuelmap.repo.CompanyRepository;
import com.gorkem.manuelmap.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User firstUser;
    private User secondUser;

    @BeforeEach
    void setUp() {
        firstUser = User.builder()
                .id(1L)
                .username("gorkem")
                .firstName("Görkem")
                .lastName("Karagöz")
                .email("gorkem@gmail.com")
                .password("123456")
                .age(22)
                .salary(7500)
                .build();

        secondUser = User.builder()
                .id(2L)
                .username("ahmet")
                .firstName("Ahmet")
                .lastName("Yılmaz")
                .email("ahmet@gmail.com")
                .password("654321")
                .age(25)
                .salary(9000)
                .build();
    }


    // getAllUser()

    @Test
    void getAllUser_shouldReturnAllUsers() {
        // Given
        List<User> userList = Arrays.asList(firstUser, secondUser);

        UserResponseDto firstDto = new UserResponseDto(
                1L, "gorkem", "Görkem", "Karagöz",
                "gorkem@gmail.com", 22, 7500, null);

        UserResponseDto secondDto = new UserResponseDto(
                2L, "ahmet", "Ahmet", "Yılmaz",
                "ahmet@gmail.com", 25, 9000, null);

        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.toResponseDto(firstUser)).thenReturn(firstDto);
        when(userMapper.toResponseDto(secondUser)).thenReturn(secondDto);

        // When
        List<UserResponseDto> result = userService.getAllUser();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(firstDto, result.get(0));
        assertEquals(secondDto, result.get(1));
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toResponseDto(firstUser);
        verify(userMapper, times(1)).toResponseDto(secondUser);
    }

    @Test
    void getAllUser_whenNoUserExist_shouldReturnEmptyList() {
        // Given
        when(userRepository.findAll()).thenReturn(List.of());

        // When
        List<UserResponseDto> result = userService.getAllUser();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
    }


    // getOneUser()

    @Test
    void getOneUser_whenUserExist_shouldReturnUser() {
        // Given
        UserResponseDto expectedDto = new UserResponseDto(
                1L, "gorkem", "Görkem", "Karagöz",
                "gorkem@gmail.com", 22, 7500, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(firstUser));
        when(userMapper.toResponseDto(firstUser)).thenReturn(expectedDto);

        // When
        UserResponseDto result = userService.getOneUser(1L);

        // Then
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).toResponseDto(firstUser);
    }

    @Test
    void getOneUser_whenUserNotFound_shouldThrowException() {
        // Given
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> userService.getOneUser(99L));
        verify(userRepository, times(1)).findById(99L);
        verify(userMapper, never()).toResponseDto(any());
    }


    // createUser()

    @Test
    void createUser_whenCompanyExists_shouldSaveAndReturnUser() {
        // Given
        UserRequestDto requestDto = new UserRequestDto(
                "gorkem", "Görkem", "Karagöz",
                "gorkem@gmail.com", "123456", 22, 7500, 1L);

        Company company = Company.builder()
                .id(1L)
                .name("Turkcell")
                .build();

        UserResponseDto expectedDto = new UserResponseDto(
                1L, "gorkem", "Görkem", "Karagöz",
                "gorkem@gmail.com", 22, 7500, "Turkcell");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(userMapper.toEntity(requestDto, company)).thenReturn(firstUser);
        when(userRepository.save(firstUser)).thenReturn(firstUser);
        when(userMapper.toResponseDto(firstUser)).thenReturn(expectedDto);

        // When
        UserResponseDto result = userService.createUser(requestDto);

        // Then
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(companyRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).toEntity(requestDto, company);
        verify(userRepository, times(1)).save(firstUser);
        verify(userMapper, times(1)).toResponseDto(firstUser);
    }

    @Test
    void createUser_whenCompanyIdIsNull_shouldSaveUserWithoutCompany() {
        // Given
        UserRequestDto requestDto = new UserRequestDto(
                "gorkem", "Görkem", "Karagöz",
                "gorkem@gmail.com", "123456", 22, 7500, null);

        UserResponseDto expectedDto = new UserResponseDto(
                1L, "gorkem", "Görkem", "Karagöz",
                "gorkem@gmail.com", 22, 7500, null);

        when(userMapper.toEntity(requestDto, null)).thenReturn(firstUser);
        when(userRepository.save(firstUser)).thenReturn(firstUser);
        when(userMapper.toResponseDto(firstUser)).thenReturn(expectedDto);

        // When
        UserResponseDto result = userService.createUser(requestDto);

        // Then
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(companyRepository, never()).findById(any());
        verify(userMapper, times(1)).toEntity(requestDto, null);
        verify(userRepository, times(1)).save(firstUser);
        verify(userMapper, times(1)).toResponseDto(firstUser);
    }

    @Test
    void createUser_whenCompanyNotFound_shouldThrowException() {
        // Given
        UserRequestDto requestDto = new UserRequestDto(
                "gorkem", "Görkem", "Karagöz",
                "gorkem@gmail.com", "123456", 22, 7500, 99L);

        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> userService.createUser(requestDto));
        verify(companyRepository, times(1)).findById(99L);
        verify(userRepository, never()).save(any());
    }


    // updateUser()

    @Test
    void updateUser_whenUserExists_shouldUpdateAndReturnUser() {
        // Given
        UserRequestDto requestDto = new UserRequestDto(
                "gorkem_updated", "Görkem", "Karagöz",
                "gorkem@gmail.com", "123456", 22, 8000, 1L);

        Company company = Company.builder()
                .id(1L)
                .name("Turkcell")
                .build();

        UserResponseDto expectedDto = new UserResponseDto(
                1L, "gorkem_updated", "Görkem", "Karagöz",
                "gorkem@gmail.com", 22, 8000, "Turkcell");

        when(userRepository.findById(1L)).thenReturn(Optional.of(firstUser));
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(userRepository.save(firstUser)).thenReturn(firstUser);
        when(userMapper.toResponseDto(firstUser)).thenReturn(expectedDto);

        // When
        UserResponseDto result = userService.updateUser(1L, requestDto);

        // Then
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(userRepository, times(1)).findById(1L);
        verify(companyRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(firstUser);
        verify(userMapper, times(1)).toResponseDto(firstUser);
    }

    @Test
    void updateUser_whenUserNotFound_shouldThrowException() {
        // Given
        UserRequestDto requestDto = new UserRequestDto(
                "gorkem", "Görkem", "Karagöz",
                "gorkem@gmail.com", "123456", 22, 7500, 1L);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> userService.updateUser(99L, requestDto));
        verify(userRepository, times(1)).findById(99L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_whenCompanyNotFound_shouldThrowException() {
        // Given
        UserRequestDto requestDto = new UserRequestDto(
                "gorkem", "Görkem", "Karagöz",
                "gorkem@gmail.com", "123456", 22, 7500, 99L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(firstUser));
        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> userService.updateUser(1L, requestDto));
        verify(userRepository, times(1)).findById(1L);
        verify(companyRepository, times(1)).findById(99L);
        verify(userRepository, never()).save(any());
    }


    // deleteOneUser()

    @Test
    void deleteOneUser_whenUserExists_shouldDeleteUser() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);

        // When
        userService.deleteOneUser(1L);

        // Then
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteOneUser_whenUserNotFound_shouldThrowException() {
        // Given
        when(userRepository.existsById(99L)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> userService.deleteOneUser(99L));
        verify(userRepository, times(1)).existsById(99L);
        verify(userRepository, never()).deleteById(any());
    }
}