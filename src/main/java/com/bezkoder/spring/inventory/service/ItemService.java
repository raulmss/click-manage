package com.bezkoder.spring.inventory.service;

import com.bezkoder.spring.inventory.dto.request.ItemRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemResponseDto;
import com.bezkoder.spring.inventory.mapper.ItemMapper;
import com.bezkoder.spring.security.jwt.models.Business;
import com.bezkoder.spring.inventory.model.Item;
import com.bezkoder.spring.inventory.model.ItemType;
import com.bezkoder.spring.inventory.repository.ItemRepository;
import com.bezkoder.spring.inventory.repository.ItemTypeRepository;
import com.bezkoder.spring.inventory.util.BusinessContextService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemTypeRepository itemTypeRepository;
    private final ItemMapper itemMapper;
    private final BusinessContextService businessContextService;

    public ItemResponseDto createItem(ItemRequestDto dto) {
        Business business = businessContextService.getCurrentBusiness();

        String typeName = dto.itemTypeRequestDto().name();
        ItemType type = itemTypeRepository.findByNameAndBusiness(typeName, business)
                .orElseThrow(() -> new EntityNotFoundException("ItemType '" + typeName + "' not found for your business."));

        Item item = itemMapper.itemRequestDtoToItem(dto);
        item.setType(type);

        return itemMapper.itemToItemResponseDto(itemRepository.save(item));
    }

    public List<ItemResponseDto> getAllItems() {
        Business business = businessContextService.getCurrentBusiness();

        return itemRepository.findByType_Business(business)
                .stream()
                .map(itemMapper::itemToItemResponseDto)
                .toList();
    }

    public ItemResponseDto getItemById(Long id) {
        Business business = businessContextService.getCurrentBusiness();
        Item item = getItemByIdEntity(id, business);

        return itemMapper.itemToItemResponseDto(item);
    }

    public ItemResponseDto updateItem(Long id, ItemRequestDto dto) {
        Business business = businessContextService.getCurrentBusiness();
        Item existing = getItemByIdEntity(id, business);

        String typeName = dto.itemTypeRequestDto().name();
        ItemType type = itemTypeRepository.findByNameAndBusiness(typeName, business)
                .orElseThrow(() -> new EntityNotFoundException("ItemType '" + typeName + "' not found for your business."));

        existing.setName(dto.name());
        existing.setDescription(dto.description());
        existing.setType(type);

        return itemMapper.itemToItemResponseDto(itemRepository.save(existing));
    }

    public void deleteItem(Long id) {
        Business business = businessContextService.getCurrentBusiness();
        Item item = getItemByIdEntity(id, business);
        itemRepository.delete(item);
    }

    private Item getItemByIdEntity(Long id, Business business) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        if (!item.getType().getBusiness().getId().equals(business.getId())) {
            throw new SecurityException("Access denied to this item.");
        }

        return item;
    }
}
