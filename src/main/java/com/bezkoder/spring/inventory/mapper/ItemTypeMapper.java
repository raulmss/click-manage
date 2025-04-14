package com.bezkoder.spring.inventory.mapper;

import com.bezkoder.spring.inventory.dto.request.ItemTypeRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemTypeResponseDto;
import com.bezkoder.spring.inventory.model.ItemType;
import com.bezkoder.spring.security.jwt.mapper.BusinessMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemTypeMapper {

    private final BusinessMapper businessMapper;

    public ItemType itemTypeRequestDtoToItemType(ItemTypeRequestDto dto) {
        if (dto == null) return null;

        ItemType itemType = new ItemType();
        itemType.setName(dto.name());
        itemType.setDescription(dto.description());

        if (dto.businessRequestDto() != null) {
            itemType.setBusiness(businessMapper.businessRequestDtoToBusiness(dto.businessRequestDto()));
        }

        return itemType;
    }

    public ItemTypeResponseDto itemTypeToItemTypeResponseDto(ItemType itemType) {
        if (itemType == null) return null;

        return new ItemTypeResponseDto(
                itemType.getId(),
                itemType.getName(),
                itemType.getDescription(),
                businessMapper.businessToBusinessResponseDto(itemType.getBusiness())
        );
    }
}
