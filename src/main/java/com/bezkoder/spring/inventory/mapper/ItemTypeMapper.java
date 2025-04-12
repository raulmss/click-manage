package com.bezkoder.spring.inventory.mapper;

import com.bezkoder.spring.inventory.dto.request.ItemTypeRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemTypeResponseDto;
import com.bezkoder.spring.inventory.model.ItemType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = BusinessMapper.class)
public interface ItemTypeMapper {

    @Mapping(source = "businessRequestDto", target = "business")
    ItemType itemTypeRequestDtoToItemType(ItemTypeRequestDto dto);

    @Mapping(source = "business", target = "businessResponseDto")
    ItemTypeResponseDto itemTypeToItemTypeResponseDto(ItemType itemType);
}
