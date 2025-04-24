package com.bezkoder.spring.inventory.service;

import com.bezkoder.spring.inventory.dto.request.ItemEntryRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemEntryResponseDto;
import com.bezkoder.spring.inventory.exception.ResourceNotFoundException;
import com.bezkoder.spring.inventory.exception.SupplierNotFoundException;
import com.bezkoder.spring.inventory.mapper.ItemEntryMapper;
import com.bezkoder.spring.inventory.model.Item;
import com.bezkoder.spring.inventory.model.ItemEntry;
import com.bezkoder.spring.inventory.model.Supplier;
import com.bezkoder.spring.inventory.repository.ItemEntryRepository;
import com.bezkoder.spring.inventory.repository.ItemRepository;
import com.bezkoder.spring.inventory.repository.SupplierRepository;
import com.bezkoder.spring.security.jwt.models.Business;
import com.bezkoder.spring.security.jwt.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ItemEntryService {

    private final ItemEntryRepository itemEntryRepository;
    private final ItemRepository itemRepository;
    private final SupplierRepository supplierRepository;
    private final ItemEntryMapper itemEntryMapper;
    private final StockService stockService;

    public ItemEntryResponseDto create(ItemEntryRequestDto dto, Business business, User user) {
        Item item = itemRepository.findById(dto.itemId())
                .filter(i -> i.getType().getBusiness().equals(business))
                .orElseThrow(() -> new ResourceNotFoundException("Item not found for this business."));

        Supplier supplier = null;
        if (dto.supplierId() != null) {
            supplier = supplierRepository.findById(dto.supplierId())
                    .filter(s -> s.getBusiness().equals(business))
                    .orElseThrow(() -> new SupplierNotFoundException("Supplier not found for this business."));
        }

        ItemEntry entry = new ItemEntry();
        entry.setItem(item);
        entry.setSupplier(supplier);
        entry.setUser(user);
        entry.setQuantity(dto.quantity());
        entry.setLotNumber(dto.lotNumber());
        entry.setExpiryDate(dto.expiryDate());
        entry.setEntryDate(Instant.now());

        stockService.addToStock(item.getId(), dto.quantity(), business);
        ItemEntry saved = itemEntryRepository.save(entry);
        return itemEntryMapper.itemEntryToItemEntryResponseDto(saved);
    }

    public Page<ItemEntryResponseDto> findAllFiltered(String itemName, String supplierName, String userName, Business business, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return itemEntryRepository.filterEntries(business, itemName, supplierName, userName, pageable)
                .map(itemEntryMapper::itemEntryToItemEntryResponseDto);
    }

    public ItemEntryResponseDto findById(Long id, Business business) {
        ItemEntry entry = itemEntryRepository.findById(id)
                .filter(e -> e.getItem().getType().getBusiness().equals(business))
                .orElseThrow(() -> new ResourceNotFoundException("Item entry not found for this business."));

        return itemEntryMapper.itemEntryToItemEntryResponseDto(entry);
    }

    public void delete(Long id, Business business) {
        ItemEntry entry = itemEntryRepository.findById(id)
                .filter(e -> e.getItem().getType().getBusiness().equals(business))
                .orElseThrow(() -> new ResourceNotFoundException("Item entry not found for this business."));

        itemEntryRepository.delete(entry);
    }
}
