package com.bezkoder.spring.inventory.service;

import com.bezkoder.spring.inventory.dto.request.ItemTypeRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemTypeResponseDto;
import com.bezkoder.spring.inventory.mapper.ItemTypeMapper;
import com.bezkoder.spring.inventory.model.Business;
import com.bezkoder.spring.inventory.model.ItemType;
import com.bezkoder.spring.inventory.repository.ItemTypeRepository;
import com.bezkoder.spring.inventory.util.BusinessContextService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemTypeService {

    private final ItemTypeRepository itemTypeRepository;
    private final ItemTypeMapper itemTypeMapper;
    private final BusinessContextService businessContextService;

    public ItemTypeResponseDto createType(ItemTypeRequestDto dto) {
        Business business = businessContextService.getCurrentBusiness();

        itemTypeRepository.findByNameAndBusiness(dto.name(), business).ifPresent(existing -> {
            throw new IllegalStateException("ItemType with name '" + dto.name() + "' already exists for your business.");
        });

        ItemType itemType = itemTypeMapper.itemTypeRequestDtoToItemType(dto);
        itemType.setBusiness(business);

        return itemTypeMapper.itemTypeToItemTypeResponseDto(itemTypeRepository.save(itemType));
    }

    public List<ItemTypeResponseDto> getAllTypes() {
        Business business = businessContextService.getCurrentBusiness();

        return itemTypeRepository.findByBusiness(business)
                .stream()
                .map(itemTypeMapper::itemTypeToItemTypeResponseDto)
                .toList();
    }

    public void deleteType(Long id) {
        Business business = businessContextService.getCurrentBusiness();

        ItemType type = itemTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ItemType with ID " + id + " not found."));

        if (!type.getBusiness().getId().equals(business.getId())) {
            throw new SecurityException("You don't own this ItemType.");
        }

        itemTypeRepository.delete(type);
    }
}
