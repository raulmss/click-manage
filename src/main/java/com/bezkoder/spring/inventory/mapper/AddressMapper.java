package com.bezkoder.spring.inventory.mapper;

import com.bezkoder.spring.inventory.dto.request.AddressRequestDto;
import com.bezkoder.spring.inventory.dto.response.AddressResponseDto;
import com.bezkoder.spring.inventory.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

    Address addressRequestDtoToAddress(AddressRequestDto dto);

    AddressResponseDto addressToAddressResponseDto(Address address);
}