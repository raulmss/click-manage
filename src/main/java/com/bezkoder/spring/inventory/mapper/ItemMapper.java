package com.bezkoder.spring.inventory.mapper;

import com.bezkoder.spring.inventory.dto.request.ItemRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemResponseDto;
import com.bezkoder.spring.inventory.model.Item;
import com.bezkoder.spring.security.jwt.mapper.BusinessMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    private final ItemTypeMapper itemTypeMapper;
    private final BusinessMapper businessMapper;

    public Item itemRequestDtoToItem(ItemRequestDto dto) {
        if (dto == null) return null;

        Item item = new Item();
        item.setName(dto.name());
        item.setDescription(dto.description());
        item.setType(itemTypeMapper.itemTypeRequestDtoToItemType(dto.itemTypeRequestDto()));
        return item;
    }

    public ItemResponseDto itemToItemResponseDto(Item item) {
        if (item == null) return null;

        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                itemTypeMapper.itemTypeToItemTypeResponseDto(item.getType())
        );
    }

}
