package com.bezkoder.spring.inventory.service;

import com.bezkoder.spring.inventory.dto.request.ItemExitRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemExitResponseDto;
import com.bezkoder.spring.inventory.exception.InvalidQuantityException;
import com.bezkoder.spring.inventory.exception.ResourceNotFoundException;
import com.bezkoder.spring.inventory.mapper.ItemExitMapper;
import com.bezkoder.spring.inventory.model.Item;
import com.bezkoder.spring.inventory.model.ItemExit;
import com.bezkoder.spring.inventory.repository.ItemExitRepository;
import com.bezkoder.spring.inventory.repository.ItemRepository;
import com.bezkoder.spring.security.jwt.models.Business;
import com.bezkoder.spring.security.jwt.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ItemExitService {

    private final ItemExitRepository itemExitRepository;
    private final ItemRepository itemRepository;
    private final ItemExitMapper itemExitMapper;
    private final StockService stockService;


    public ItemExitResponseDto create(ItemExitRequestDto dto, Business business, User user) {

        if (dto.quantity() <= 0) {
            throw new InvalidQuantityException("Quantity must be a positive number.");
        }

        Item item = itemRepository.findByIdAndType_Business(dto.itemId(), business)
                .orElseThrow(() -> new ResourceNotFoundException("Item with ID " + dto.itemId() + " not found"));

        ItemExit exit = itemExitMapper.itemExitRequestDtoToItemExit(dto);
        exit.setItem(item);
        exit.setUser(user);
        exit.setExitDate(Instant.now());

        stockService.subtractFromStock(exit.getItem().getId(), dto.quantity(), business);
        return itemExitMapper.itemExitToItemExitResponseDto(itemExitRepository.save(exit));
    }

    public Page<ItemExitResponseDto> findAllFiltered(String itemName, String userName, Business business, Pageable pageable) {
        return itemExitRepository.filterItemExits(itemName, userName, business, pageable)
                .map(itemExitMapper::itemExitToItemExitResponseDto);
    }

    public ItemExitResponseDto findById(Long id, Business business) {
        ItemExit exit = itemExitRepository.findByIdAndItemTypeBusiness(id, business)
                .orElseThrow(() -> new ResourceNotFoundException("ItemExit with ID " + id + " not found"));

        return itemExitMapper.itemExitToItemExitResponseDto(exit);
    }

    public void deleteById(Long id, Business business) {
        ItemExit exit = itemExitRepository.findByIdAndItemTypeBusiness(id, business)
                .orElseThrow(() -> new ResourceNotFoundException("ItemExit with ID " + id + " not found"));

        itemExitRepository.delete(exit);
    }
}
