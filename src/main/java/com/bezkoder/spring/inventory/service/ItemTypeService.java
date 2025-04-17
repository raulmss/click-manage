package com.bezkoder.spring.inventory.service;

import com.bezkoder.spring.inventory.dto.request.ItemTypeRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemTypeResponseDto;
import com.bezkoder.spring.inventory.exception.ItemTypeAlreadyExistsException;
import com.bezkoder.spring.inventory.mapper.ItemTypeMapper;
import com.bezkoder.spring.inventory.model.ItemType;
import com.bezkoder.spring.inventory.repository.ItemTypeRepository;
import com.bezkoder.spring.security.jwt.models.Business;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemTypeService {

    private final ItemTypeRepository itemTypeRepository;
    private final ItemTypeMapper itemTypeMapper;

    public ItemTypeResponseDto create(ItemTypeRequestDto dto, Business business) {
        if (itemTypeRepository.existsByNameIgnoreCaseAndBusiness(dto.name(), business)) {
            throw new ItemTypeAlreadyExistsException(dto.name());
        }

        ItemType type = itemTypeMapper.itemTypeRequestDtoToItemType(dto);
        type.setBusiness(business);
        return itemTypeMapper.itemTypeToItemTypeResponseDto(itemTypeRepository.save(type));
    }

    public Page<ItemTypeResponseDto> findAllByBusiness(Business business, Pageable pageable) {
        return itemTypeRepository.findByBusiness(business, pageable)
                .map(itemTypeMapper::itemTypeToItemTypeResponseDto);
    }

    public ItemTypeResponseDto findByIdAndBusiness(Long id, Business business) {
        ItemType type = itemTypeRepository.findByIdAndBusiness(id, business)
                .orElseThrow(() -> new EntityNotFoundException("Item type not found for this business"));
        return itemTypeMapper.itemTypeToItemTypeResponseDto(type);
    }

    public ItemTypeResponseDto update(Long id, ItemTypeRequestDto dto, Business business) {
        ItemType existing = itemTypeRepository.findByIdAndBusiness(id, business)
                .orElseThrow(() -> new EntityNotFoundException("Item type not found for this business"));
        existing.setName(dto.name());
        existing.setDescription(dto.description());
        return itemTypeMapper.itemTypeToItemTypeResponseDto(itemTypeRepository.save(existing));
    }

    public void delete(Long id, Business business) {
        ItemType type = itemTypeRepository.findByIdAndBusiness(id, business)
                .orElseThrow(() -> new EntityNotFoundException("Item type not found for this business"));
        itemTypeRepository.delete(type);
    }

    public Page<ItemTypeResponseDto> filterItemTypeByName(Business business, String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return itemTypeRepository.filterItemTypes(business, name, pageable)
                .map(itemTypeMapper::itemTypeToItemTypeResponseDto);
    }

    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }

}
