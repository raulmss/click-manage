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
        return ItemType.builder()
                .name(dto.name())
                .description(dto.description())
                .business(businessMapper.businessRequestDtoToBusiness(dto.business()))
                .build();
    }

    public ItemTypeResponseDto itemTypeToItemTypeResponseDto(ItemType itemType) {
        return new ItemTypeResponseDto(
                itemType.getId(),
                itemType.getName(),
                itemType.getDescription(),
                businessMapper.businessToBusinessResponseDto(itemType.getBusiness())
        );
    }
}
