package com.bezkoder.spring.inventory.mapper;

import com.bezkoder.spring.inventory.dto.request.ItemEntryRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemEntryResponseDto;
import com.bezkoder.spring.inventory.model.ItemEntry;
import com.bezkoder.spring.inventory.model.Item;
import com.bezkoder.spring.inventory.model.Supplier;
import com.bezkoder.spring.security.jwt.mapper.UserMapper;
import com.bezkoder.spring.security.jwt.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemEntryMapper {

    private final ItemMapper itemMapper;
    private final SupplierMapper supplierMapper;
    private final UserMapper userMapper;

    public ItemEntry itemEntryRequestDtoToItemEntry(ItemEntryRequestDto dto, Item item, Supplier supplier, User user) {
        ItemEntry entry = new ItemEntry();
        entry.setItem(item);
        entry.setSupplier(supplier);
        entry.setUser(user);
        entry.setQuantity(dto.quantity());
        entry.setExpiryDate(dto.expiryDate());
        entry.setLotNumber(dto.lotNumber());
        // entryDate is set server-side
        return entry;
    }

    public ItemEntryResponseDto itemEntryToItemEntryResponseDto(ItemEntry entry) {
        return new ItemEntryResponseDto(
                entry.getId(),
                itemMapper.itemToItemResponseDto(entry.getItem()),
                entry.getSupplier() != null ? supplierMapper.supplierToSupplierResponseDto(entry.getSupplier()) : null,
                entry.getUser() != null ? userMapper.userToUserSummaryDto(entry.getUser()) : null,
                entry.getQuantity(),
                entry.getLotNumber(),
                entry.getExpiryDate(),
                entry.getEntryDate()
        );
    }
}

