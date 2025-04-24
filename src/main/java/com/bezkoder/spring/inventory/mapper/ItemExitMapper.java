package com.bezkoder.spring.inventory.mapper;

import com.bezkoder.spring.inventory.dto.request.ItemExitRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemExitResponseDto;
import com.bezkoder.spring.inventory.model.ItemExit;
import com.bezkoder.spring.security.jwt.mapper.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class ItemExitMapper {

    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    public ItemExitMapper(ItemMapper itemMapper, UserMapper userMapper) {
        this.itemMapper = itemMapper;
        this.userMapper = userMapper;
    }

    public ItemExit itemExitRequestDtoToItemExit(ItemExitRequestDto dto) {
        if (dto == null) return null;

        ItemExit exit = new ItemExit();
        exit.setQuantity(dto.quantity());
        exit.setLotNumber(dto.lotNumber());
        exit.setReason(dto.reason());
        // item and user will be set in service layer
        return exit;
    }

    public ItemExitResponseDto itemExitToItemExitResponseDto(ItemExit exit) {
        if (exit == null) return null;

        return new ItemExitResponseDto(
                exit.getId(),
                itemMapper.itemToItemResponseDto(exit.getItem()),
                exit.getQuantity(),
                exit.getLotNumber(),
                exit.getReason(),
                exit.getExitDate(),
                userMapper.userToUserSummaryDto(exit.getUser())
        );
    }
}
