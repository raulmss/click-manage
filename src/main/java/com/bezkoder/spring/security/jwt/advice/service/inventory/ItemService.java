package com.bezkoder.spring.security.jwt.advice.service.inventory;


import com.bezkoder.spring.security.jwt.models.User;
import com.bezkoder.spring.security.jwt.models.inventory.Item;
import com.bezkoder.spring.security.jwt.models.inventory.ItemType;
import com.bezkoder.spring.security.jwt.payload.request.inventory.ItemRequestDto;
import com.bezkoder.spring.security.jwt.payload.response.inventory.ItemResponseDto;
import com.bezkoder.spring.security.jwt.repository.UserRepository;
import com.bezkoder.spring.security.jwt.repository.inventory.ItemRepository;
import com.bezkoder.spring.security.jwt.repository.inventory.ItemTypeRepository;
import com.bezkoder.spring.security.jwt.security.services.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemTypeRepository itemTypeRepository;

    public ItemResponseDto createItem(ItemRequestDto dto) {
        Long businessId = getCurrentBusinessId();

        ItemType type = itemTypeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new EntityNotFoundException("ItemType not found"));

        if (!type.getBusiness().getId().equals(businessId)) {
            throw new SecurityException("You don't own this ItemType.");
        }

        Item item = Item.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .type(type)
                .businessId(businessId)
                .build();

        Item saved = itemRepository.save(item);
        return toDto(saved);
    }

    public List<ItemResponseDto> getAllItems() {
        Long businessId = getCurrentBusinessId();
        return itemRepository.findByBusinessId(businessId)
                .stream().map(this::toDto)
                .collect(Collectors.toList());
    }

    public ItemResponseDto getItemById(Long id) {
        Long businessId = getCurrentBusinessId();
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        if (!item.getBusinessId().equals(businessId)) {
            throw new SecurityException("Access denied to item.");
        }

        return toDto(item);
    }

    public ItemResponseDto updateItem(Long id, ItemRequestDto dto) {
        Item existing = getItemByIdEntity(id); // reuse check

        ItemType type = itemTypeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new EntityNotFoundException("ItemType not found"));

        if (!type.getBusiness().getId().equals(getCurrentBusinessId())) {
            throw new SecurityException("Invalid type for this business");
        }

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setType(type);

        Item saved = itemRepository.save(existing);
        return toDto(saved);
    }

    public void deleteItem(Long id) {
        Item item = getItemByIdEntity(id);
        itemRepository.delete(item);
    }

    // --- Helpers ---

    private ItemResponseDto toDto(Item item) {
        ItemResponseDto dto = new ItemResponseDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setTypeDescription(item.getType().getDescription());
        return dto;
    }

    private Item getItemByIdEntity(Long id) {
        Long businessId = getCurrentBusinessId();
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        if (!item.getBusinessId().equals(businessId)) {
            throw new SecurityException("Access denied");
        }
        return item;
    }

    private Long getCurrentBusinessId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return user.getBusiness().getId();
    }
}
