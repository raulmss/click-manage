package com.bezkoder.spring.inventory.service;

import com.bezkoder.spring.inventory.dto.request.ItemEntryRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemEntryResponseDto;
import com.bezkoder.spring.inventory.exception.ResourceNotFoundException;
import com.bezkoder.spring.inventory.exception.SupplierNotFoundException;
import com.bezkoder.spring.inventory.mapper.ItemEntryMapper;
import com.bezkoder.spring.inventory.model.*;
import com.bezkoder.spring.inventory.repository.ItemEntryRepository;
import com.bezkoder.spring.inventory.repository.ItemRepository;
import com.bezkoder.spring.inventory.repository.SupplierRepository;
import com.bezkoder.spring.security.jwt.models.Business;
import com.bezkoder.spring.security.jwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemEntryServiceTest {

    @Mock private ItemEntryRepository itemEntryRepository;
    @Mock private ItemRepository itemRepository;
    @Mock private SupplierRepository supplierRepository;
    @Mock private ItemEntryMapper itemEntryMapper;
    @Mock private StockService stockService;

    @InjectMocks private ItemEntryService itemEntryService;

    private final Business business = new Business();
    private final User user = new User();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldSucceed_withSupplier() {
        Item item = new Item();
        ItemType type = new ItemType();
        type.setBusiness(business);
        item.setType(type);
        item.setId(10L);

        Supplier supplier = new Supplier();
        supplier.setId(1L);
        supplier.setBusiness(business);

        ItemEntry savedEntry = new ItemEntry();
        ItemEntryRequestDto requestDto = new ItemEntryRequestDto(
                10L, 1L, 5, "LOT123", LocalDate.now()
        );

        when(itemRepository.findById(10L)).thenReturn(Optional.of(item));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(itemEntryRepository.save(any())).thenReturn(savedEntry);
        ItemEntryResponseDto response = mock(ItemEntryResponseDto.class);
        when(itemEntryMapper.itemEntryToItemEntryResponseDto(savedEntry)).thenReturn(response);

        ItemEntryResponseDto result = itemEntryService.create(requestDto, business, user);

        assertEquals(response, result);
        verify(stockService).addToStock(10L, 5, business);
        verify(itemEntryRepository).save(any());
    }


    @Test
    void create_shouldSucceed_withoutSupplier() {
        Item item = new Item();
        ItemType type = new ItemType();
        type.setBusiness(business);
        item.setType(type);
        item.setId(10L);

        ItemEntry savedEntry = new ItemEntry();
        ItemEntryRequestDto requestDto = new ItemEntryRequestDto(
                10L, null, 2, "LOT999", LocalDate.now()
        );

        when(itemRepository.findById(10L)).thenReturn(Optional.of(item));
        when(itemEntryRepository.save(any())).thenReturn(savedEntry);
        when(itemEntryMapper.itemEntryToItemEntryResponseDto(savedEntry)).thenReturn(mock(ItemEntryResponseDto.class));

        ItemEntryResponseDto result = itemEntryService.create(requestDto, business, user);

        assertNotNull(result);
        verify(stockService).addToStock(10L, 2, business);
        verify(itemEntryRepository).save(any());
    }


    @Test
    void create_shouldThrowIfItemNotFoundOrMismatched() {
        when(itemRepository.findById(10L)).thenReturn(Optional.empty());

        ItemEntryRequestDto requestDto = new ItemEntryRequestDto(
                10L, 1L, 3, "LOTFAIL", LocalDate.now()
        );

        assertThrows(ResourceNotFoundException.class, () -> itemEntryService.create(requestDto, business, user));
    }

    @Test
    void create_shouldThrowIfSupplierNotFoundOrMismatched() {
        Item item = new Item();
        ItemType type = new ItemType();
        type.setBusiness(business);
        item.setType(type);
        item.setId(10L);

        Business otherBusiness = new Business(); // different object
        Supplier supplier = new Supplier();
        supplier.setBusiness(otherBusiness);

        ItemEntryRequestDto requestDto = new ItemEntryRequestDto(
                10L, 5L, 3, "LOTMISMATCH", LocalDate.now()
        );

        when(itemRepository.findById(10L)).thenReturn(Optional.of(item));
        when(supplierRepository.findById(5L)).thenReturn(Optional.of(supplier));

        assertThrows(SupplierNotFoundException.class, () -> itemEntryService.create(requestDto, business, user));
    }


    @Test
    void findAllFiltered_shouldReturnMappedPage() {
        Business business = new Business();
        ItemEntry entry = new ItemEntry();
        Page<ItemEntry> page = new PageImpl<>(List.of(entry));
        ItemEntryResponseDto response = mock(ItemEntryResponseDto.class);

        when(itemEntryRepository.filterEntries(eq(business), any(), any(), any(), any()))
                .thenReturn(page);
        when(itemEntryMapper.itemEntryToItemEntryResponseDto(entry)).thenReturn(response);

        Page<ItemEntryResponseDto> result = itemEntryService.findAllFiltered(
                "item", "supplier", "user", business, 0, 5);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void findById_shouldReturnEntry() {
        ItemEntry entry = new ItemEntry();
        Item item = new Item();
        ItemType type = new ItemType();
        type.setBusiness(business);
        item.setType(type);
        entry.setItem(item);

        when(itemEntryRepository.findById(1L)).thenReturn(Optional.of(entry));
        ItemEntryResponseDto response = mock(ItemEntryResponseDto.class);
        when(itemEntryMapper.itemEntryToItemEntryResponseDto(entry)).thenReturn(response);

        ItemEntryResponseDto result = itemEntryService.findById(1L, business);
        assertEquals(response, result);
    }

    @Test
    void findById_shouldThrowIfNotFoundOrMismatched() {
        when(itemEntryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> itemEntryService.findById(1L, business));
    }

    @Test
    void delete_shouldSucceed() {
        ItemEntry entry = new ItemEntry();
        Item item = new Item();
        ItemType type = new ItemType();
        type.setBusiness(business);
        item.setType(type);
        entry.setItem(item);

        when(itemEntryRepository.findById(1L)).thenReturn(Optional.of(entry));

        itemEntryService.delete(1L, business);
        verify(itemEntryRepository).delete(entry);
    }

    @Test
    void delete_shouldThrowIfNotFoundOrMismatched() {
        when(itemEntryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> itemEntryService.delete(1L, business));
    }
}
