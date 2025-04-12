package com.bezkoder.spring.inventory.mapper;

import com.bezkoder.spring.inventory.dto.request.ItemRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemResponseDto;
import com.bezkoder.spring.inventory.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ItemTypeMapper.class, BusinessMapper.class})
public interface ItemMapper {

    @Mapping(source = "itemTypeRequestDto", target = "type")
    @Mapping(source = "businessRequestDto", target = "business")
    Item itemRequestDtoToItem(ItemRequestDto dto);

    @Mapping(source = "type", target = "itemTypeResponseDto")
    @Mapping(source = "business", target = "businessResponseDto")
    ItemResponseDto itemToItemResponseDto(Item item);
}
