package com.bezkoder.spring.inventory.service;

import com.bezkoder.spring.security.jwt.models.Business;
import com.bezkoder.spring.security.jwt.models.User;
import com.bezkoder.spring.inventory.model.ItemType;
import com.bezkoder.spring.inventory.dto.request.ItemTypeRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemTypeResponseDto;
import com.bezkoder.spring.security.jwt.repository.BusinessRepository;
import com.bezkoder.spring.security.jwt.repository.UserRepository;
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
public class ItemTypeService {

    private final ItemTypeRepository itemTypeRepository;
    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;

    public ItemTypeResponseDto createType(ItemTypeRequestDto dto) {
        Business business = getCurrentBusiness();

        ItemType type = ItemType.builder()
                .description(dto.description())
                .business(business)
                .build();

        return toDto(itemTypeRepository.save(type));
    }

    public List<ItemTypeResponseDto> getAllTypes() {
        Business business = getCurrentBusiness();
        return itemTypeRepository.findByBusiness(business)
                .stream().map(this::toDto)
                .collect(Collectors.toList());
    }

    public void deleteType(Long id) {
        Business business = getCurrentBusiness();
        ItemType type = itemTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ItemType not found"));

        if (!type.getBusiness().getId().equals(business.getId())) {
            throw new SecurityException("You don't own this ItemType.");
        }

        itemTypeRepository.delete(type);
    }

    private ItemTypeResponseDto toDto(ItemType type) {
        return new ItemTypeResponseDto(type.getId(), type.getDescription());
    }

    private Business getCurrentBusiness() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return user.getBusiness();
    }
}
