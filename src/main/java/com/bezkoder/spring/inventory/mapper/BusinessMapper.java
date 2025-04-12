package com.bezkoder.spring.inventory.mapper;

import com.bezkoder.spring.inventory.dto.request.BusinessRequestDto;
import com.bezkoder.spring.inventory.dto.response.BusinessResponseDto;
import com.bezkoder.spring.inventory.model.Business;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = AddressMapper.class)
public interface BusinessMapper {

    @Mapping(source = "addressRequestDto", target = "address")
    Business businessRequestDtoToBusiness(BusinessRequestDto dto);

    @Mapping(source = "address", target = "addressResponseDto")
    BusinessResponseDto businessToBusinessResponseDto(Business business);
}