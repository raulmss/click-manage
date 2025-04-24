package com.bezkoder.spring.security.jwt.mapper;

import com.bezkoder.spring.security.jwt.payload.request.BusinessRequestDto;
import com.bezkoder.spring.security.jwt.payload.response.BusinessResponseDto;
import com.bezkoder.spring.inventory.mapper.AddressMapper;
import com.bezkoder.spring.security.jwt.models.Business;
import com.bezkoder.spring.security.jwt.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BusinessMapper {

    private final AddressMapper addressMapper;
    private final UserMapper userMapper; // assumed to exist or to be implemented

    public Business businessRequestDtoToBusiness(BusinessRequestDto dto) {
        if (dto == null) return null;

        Business business = new Business();
        business.setName(dto.name());
        business.setIndustry(dto.industry());

        if (dto.addressRequestDto() != null) {
            business.setAddress(addressMapper.addressRequestDtoToAddress(dto.addressRequestDto()));
        }

        if (dto.usersRequestDtoList() != null) {
            List<User> users = dto.usersRequestDtoList().stream()
                    .map(userMapper::userRequestDtoToUser)
                    .collect(Collectors.toList());
            business.setUsers(users);
        }

        return business;
    }

    public BusinessResponseDto businessToBusinessResponseDto(Business business) {
        if (business == null) return null;

        return new BusinessResponseDto(
                business.getId(),
                business.getName(),
                business.getIndustry(),
                business.getAddress() != null ?
                        addressMapper.addressToAddressResponseDto(business.getAddress()) :
                        null
        );
    }
}
