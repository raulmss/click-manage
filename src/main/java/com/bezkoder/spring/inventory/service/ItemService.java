package com.bezkoder.spring.inventory.service;

import com.bezkoder.spring.inventory.dto.request.ItemRequestDto;
import com.bezkoder.spring.inventory.dto.request.StockRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemResponseDto;
import com.bezkoder.spring.inventory.exception.ItemAlreadyExistsException;
import com.bezkoder.spring.inventory.mapper.ItemMapper;
import com.bezkoder.spring.inventory.mapper.ItemTypeMapper;
import com.bezkoder.spring.inventory.mapper.StockMapper;
import com.bezkoder.spring.inventory.model.Item;
import com.bezkoder.spring.inventory.model.Stock;
import com.bezkoder.spring.inventory.repository.ItemRepository;
import com.bezkoder.spring.inventory.repository.ItemTypeRepository;
import com.bezkoder.spring.security.jwt.models.Business;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final ItemTypeMapper itemTypeMapper;
    private final ItemTypeRepository itemTypeRepository;
    private final StockService stockService;
    private final StockMapper stockMapper;

    public ItemResponseDto create(ItemRequestDto dto, Business business) {
        if (itemRepository.existsByBarCodeIgnoreCaseAndType_Business(dto.barCode(), business)) {
            throw new ItemAlreadyExistsException("Item with bar code '%s' already exists.".formatted(dto.barCode()));
        }

        if (itemRepository.existsByNameIgnoreCaseAndType_Business(dto.name(), business)) {
            throw new ItemAlreadyExistsException("An item with the name '%s' already exists for this business.".formatted(dto.name()));
        }

        Item item = itemMapper.itemRequestDtoToItem(dto);
        item.setType(itemTypeRepository.findByNameAndBusiness(dto.type().name(), business)
                .orElseThrow(() -> new EntityNotFoundException("Item type not found")));
        item.getType().setBusiness(business); // ensure type is tied to the business
        Item savedItem = itemRepository.save(item);

        // Create stock for the item
        stockService.createStock(new StockRequestDto(savedItem.getId(), null), business);
        return itemMapper.itemToItemResponseDto(savedItem);
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
        existing.setType(itemTypeRepository.findByNameAndBusiness(dto.type().name(), business)
                .orElseThrow(() -> new EntityNotFoundException("Item type not found")));
        return itemMapper.itemToItemResponseDto(itemRepository.save(existing));
    }

    public void delete(Long id, Business business) {
        Item item = itemRepository.findByIdAndType_Business(id, business)
                .orElseThrow(() -> new EntityNotFoundException("Item not found for this business"));
        itemRepository.delete(item);
    }

    public Page<ItemResponseDto> filterItems(Business business, String name, String barCode, String itemTypeName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return itemRepository.filterItems(business, name, barCode, itemTypeName, pageable)
                .map(itemMapper::itemToItemResponseDto);
    }



}
