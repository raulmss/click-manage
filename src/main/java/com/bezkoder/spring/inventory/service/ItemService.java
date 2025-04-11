package com.bezkoder.spring.inventory.service;

import com.bezkoder.spring.security.jwt.models.User;
import com.bezkoder.spring.inventory.model.Item;
import com.bezkoder.spring.inventory.model.ItemType;
import com.bezkoder.spring.inventory.dto.request.ItemRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemResponseDto;
import com.bezkoder.spring.security.jwt.repository.UserRepository;
import com.bezkoder.spring.inventory.repository.ItemRepository;
import com.bezkoder.spring.inventory.repository.ItemTypeRepository;
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

        ItemType type = itemTypeRepository.findById(dto.typeId())
                .orElseThrow(() -> new EntityNotFoundException("ItemType not found"));

        if (!type.getBusiness().getId().equals(businessId)) {
            throw new SecurityException("You don't own this ItemType.");
        }

        Item item = Item.builder()
                .name(dto.name())
                .description(dto.description())
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
        Item existing = getItemByIdEntity(id);

        ItemType type = itemTypeRepository.findById(dto.typeId())
                .orElseThrow(() -> new EntityNotFoundException("ItemType not found"));

        if (!type.getBusiness().getId().equals(getCurrentBusinessId())) {
            throw new SecurityException("Invalid type for this business");
        }

        existing.setName(dto.name());
        existing.setDescription(dto.description());
        existing.setType(type);

        Item saved = itemRepository.save(existing);
        return toDto(saved);
    }

    public void deleteItem(Long id) {
        Item item = getItemByIdEntity(id);
        itemRepository.delete(item);
    }

    private ItemResponseDto toDto(Item item) {
        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getType().getDescription()
        );
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
