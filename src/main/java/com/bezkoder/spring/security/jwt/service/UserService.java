package com.bezkoder.spring.security.jwt.service;

import com.bezkoder.spring.inventory.exception.ResourceNotFoundException;
import com.bezkoder.spring.security.jwt.mapper.UserMapper;
import com.bezkoder.spring.security.jwt.models.Business;
import com.bezkoder.spring.security.jwt.models.User;
import com.bezkoder.spring.security.jwt.payload.response.UserResponseDto;
import com.bezkoder.spring.security.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Page<UserResponseDto> filterUsers(String username, String email, String roleName,
                                             int page, int size, Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "username"));

        return userRepository.findAll(pageable).stream()
                .filter(user -> username == null || user.getUsername().toLowerCase(Locale.ROOT).contains(username.toLowerCase()))
                .filter(user -> email == null || user.getEmail().toLowerCase(Locale.ROOT).contains(email.toLowerCase()))
                .filter(user -> roleName == null ||
                        user.getRoles().stream()
                                .anyMatch(role -> role.getName().name().toLowerCase().contains(roleName.toLowerCase())))
                .map(userMapper::userToUserResponseDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        list -> new PageImpl<>(list, pageable, list.size())));
    }

    public Page<UserResponseDto> findAllFiltered(
            String username,
            String email,
            String role,
            Business business,
            int page,
            int size,
            String sortDir,
            String sortField
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(sortDir), sortField)
        );

        return userRepository.filterUsers(business, username, email, role, pageable)
                .map(userMapper::userToUserResponseDto);
    }

    public Page<UserResponseDto> filterAllUsersForAdmin(
            String username,
            String email,
            String role,
            String businessName,
            int page,
            int size,
            String sortDir // "asc" or "desc"
    ) {
        Sort.Direction direction = Sort.Direction.fromOptionalString(sortDir).orElse(Sort.Direction.ASC);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "username"));

        return userRepository.filterAllUsersForAdmin(username, email, role, businessName, pageable)
                .map(userMapper::userToUserResponseDto);
    }


    public UserResponseDto findById(Long id, Business business) {
        User user = userRepository.findById(id)
                .filter(u -> u.getBusiness().equals(business))
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found for this business."));
        return userMapper.userToUserResponseDto(user);
    }


    public UserResponseDto findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username '" + username + "' not found."));
        return userMapper.userToUserResponseDto(user);
    }

    public void deleteByUsername(String username, Business business) {
        User user = userRepository.findByUsernameAndBusiness(username, business)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this business."));

        userRepository.delete(user);
    }
}
