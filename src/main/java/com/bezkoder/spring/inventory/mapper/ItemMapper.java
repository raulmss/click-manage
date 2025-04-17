package com.bezkoder.spring.inventory.mapper;

import com.bezkoder.spring.inventory.dto.request.ItemRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemResponseDto;
import com.bezkoder.spring.inventory.model.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    private final ItemTypeMapper itemTypeMapper;

    public Item itemRequestDtoToItem(ItemRequestDto dto) {
        return Item.builder()
                .name(dto.name())
                .description(dto.description())
                .barCode(dto.barCode())
                .type(itemTypeMapper.itemTypeRequestDtoToItemType(dto.type()))
                .build();
    }

    public ItemResponseDto itemToItemResponseDto(Item item) {
        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getBarCode(),
                itemTypeMapper.itemTypeToItemTypeResponseDto(item.getType())
        );
    }
}
