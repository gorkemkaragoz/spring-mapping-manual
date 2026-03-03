package com.gorkem.manuelmap.serviceImpl;

import com.gorkem.manuelmap.dto.request.UserRequestDto;
import com.gorkem.manuelmap.dto.response.UserResponseDto;
import com.gorkem.manuelmap.entity.Company;
import com.gorkem.manuelmap.entity.User;
import com.gorkem.manuelmap.mapper.UserMapper;
import com.gorkem.manuelmap.repo.CompanyRepository;
import com.gorkem.manuelmap.repo.UserRepository;
import com.gorkem.manuelmap.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponseDto> getAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> userMapper.toResponseDto(user))
                .toList();
    }

    @Override
    public UserResponseDto getOneUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        Company company = getCompanyIfExists(userRequestDto.companyId());
        User userToSave = userMapper.toEntity(userRequestDto, company);
        User savedUser = userRepository.save(userToSave);
        return userMapper.toResponseDto(savedUser);
    }

    @Override
    public UserResponseDto updateUser(Long userId, UserRequestDto userRequestDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        existingUser.setUsername(userRequestDto.username());
        existingUser.setFirstName(userRequestDto.firstName());
        existingUser.setLastName(userRequestDto.lastName());
        existingUser.setEmail(userRequestDto.email());
        existingUser.setPassword(userRequestDto.password());
        existingUser.setAge(userRequestDto.age());
        existingUser.setSalary(userRequestDto.salary());
        existingUser.setCompany(getCompanyIfExists(userRequestDto.companyId()));

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    public void deleteOneUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }

    private Company getCompanyIfExists(Long companyId) {
        if (companyId == null) {
            return null;
        }
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));
    }
}