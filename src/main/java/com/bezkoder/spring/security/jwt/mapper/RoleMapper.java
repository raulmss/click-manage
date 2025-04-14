package com.bezkoder.spring.security.jwt.mapper;

import com.bezkoder.spring.security.jwt.models.Role;
import com.bezkoder.spring.security.jwt.payload.request.RoleRequestDto;
import com.bezkoder.spring.security.jwt.payload.response.RoleResponseDto;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role roleRequestDtoToRole(RoleRequestDto dto) {
        if (dto == null) return null;

        Role role = new Role();
        role.setName(dto.name());
        return role;
    }

    public RoleResponseDto roleToRoleResponseDto(Role role) {
        if (role == null) return null;

        return new RoleResponseDto(
                role.getId(),
                role.getName()
        );
    }
}
