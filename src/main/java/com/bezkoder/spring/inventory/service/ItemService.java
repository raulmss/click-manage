package com.bezkoder.spring.inventory.service;

import com.bezkoder.spring.inventory.dto.request.ItemRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemResponseDto;
import com.bezkoder.spring.inventory.mapper.ItemMapper;
import com.bezkoder.spring.inventory.mapper.ItemTypeMapper;
import com.bezkoder.spring.inventory.model.Item;
import com.bezkoder.spring.inventory.repository.ItemRepository;
import com.bezkoder.spring.security.jwt.models.Business;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final ItemTypeMapper itemTypeMapper;

    public ItemResponseDto create(ItemRequestDto dto, Business business) {
        Item item = itemMapper.itemRequestDtoToItem(dto);
        item.getType().setBusiness(business); // ensure type is tied to the business
        return itemMapper.itemToItemResponseDto(itemRepository.save(item));
    }

    public Page<ItemResponseDto> findAllByBusiness(Business business, Pageable pageable) {
        return itemRepository.findByType_Business(business, pageable)
                .map(itemMapper::itemToItemResponseDto);
    }

    public ItemResponseDto findByIdAndBusiness(Long id, Business business) {
        Item item = itemRepository.findByIdAndType_Business(id, business)
                .orElseThrow(() -> new EntityNotFoundException("Item not found for this business"));
        return itemMapper.itemToItemResponseDto(item);
    }

    public ItemResponseDto update(Long id, ItemRequestDto dto, Business business) {
        Item existing = itemRepository.findByIdAndType_Business(id, business)
                .orElseThrow(() -> new EntityNotFoundException("Item not found for this business"));
        existing.setName(dto.name());
        existing.setDescription(dto.description());
        existing.setBarCode(dto.barCode());
        existing.setType(itemTypeMapper.itemTypeRequestDtoToItemType(dto.type()));
        return itemMapper.itemToItemResponseDto(itemRepository.save(existing));
    }

    public void delete(Long id, Business business) {
        Item item = itemRepository.findByIdAndType_Business(id, business)
                .orElseThrow(() -> new EntityNotFoundException("Item not found for this business"));
        itemRepository.delete(item);
    }
}
