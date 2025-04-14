package com.bezkoder.spring.security.jwt.mapper;


import com.bezkoder.spring.security.jwt.models.Business;
import com.bezkoder.spring.security.jwt.models.Role;
import com.bezkoder.spring.security.jwt.models.User;
import com.bezkoder.spring.security.jwt.payload.request.UserRequestDto;
import com.bezkoder.spring.security.jwt.payload.response.RoleResponseDto;
import com.bezkoder.spring.security.jwt.payload.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final RoleMapper roleMapper;

    public User userRequestDtoToUser(UserRequestDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());

        if (dto.roles() != null) {
            Set<Role> roles = dto.roles().stream()
                    .map(roleMapper::roleRequestDtoToRole)
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        return user;
    }

    public UserResponseDto userToUserResponseDto(User user) {
        if (user == null) return null;

        Set<RoleResponseDto> roleDtos = null;
        if (user.getRoles() != null) {
            roleDtos = user.getRoles().stream()
                    .map(roleMapper::roleToRoleResponseDto)
                    .collect(Collectors.toSet());
        }

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roleDtos
        );
    }
}
