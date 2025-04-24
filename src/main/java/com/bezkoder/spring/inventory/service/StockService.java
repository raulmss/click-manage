package com.bezkoder.spring.inventory.service;

import com.bezkoder.spring.inventory.dto.request.StockRequestDto;
import com.bezkoder.spring.inventory.dto.response.StockResponseDto;
import com.bezkoder.spring.inventory.exception.InsufficientStockException;
import com.bezkoder.spring.inventory.exception.ResourceNotFoundException;
import com.bezkoder.spring.inventory.exception.StockAlreadyExistsException;
import com.bezkoder.spring.inventory.mapper.StockMapper;
import com.bezkoder.spring.inventory.model.Item;
import com.bezkoder.spring.inventory.model.Stock;
import com.bezkoder.spring.inventory.repository.ItemRepository;
import com.bezkoder.spring.inventory.repository.StockRepository;
import com.bezkoder.spring.security.jwt.models.Business;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final ItemRepository itemRepository;
    private final StockMapper stockMapper;

    public StockResponseDto createStock(StockRequestDto dto, Business business) {
        Item item = itemRepository.findByIdAndType_Business(dto.itemId(), business)
                .orElseThrow(() -> new ResourceNotFoundException("Item with ID " + dto.itemId() + " not found"));

        if (stockRepository.existsByItem(item)) {
            throw new StockAlreadyExistsException("Stock entry already exists for this item.");
        }

        Stock stock = stockMapper.stockRequestDtoToStock(dto, item);
        stock.setQuantity(0);
        return stockMapper.stockToStockResponseDto(stockRepository.save(stock));
    }

    public StockResponseDto getStockById(Long id, Business business) {
        Stock stock = stockRepository.findByIdAndItem_Type_Business(id, business)
                .orElseThrow(() -> new ResourceNotFoundException("Stock with ID " + id + " not found."));
        return stockMapper.stockToStockResponseDto(stock);
    }

    public StockResponseDto updateStock(Long id, StockRequestDto dto, Business business) {
        Stock stock = stockRepository.findByIdAndItem_Type_Business(id, business)
                .orElseThrow(() -> new ResourceNotFoundException("Stock with ID " + id + " not found."));

        return stockMapper.stockToStockResponseDto(stockRepository.save(stock));
    }

    public void deleteStock(Long id, Business business) {
        Stock stock = stockRepository.findByIdAndItem_Type_Business(id, business)
                .orElseThrow(() -> new ResourceNotFoundException("Stock with ID " + id + " not found."));
        stockRepository.delete(stock);
    }
    @Transactional
    public StockResponseDto addToStock(Long itemId, int quantity, Business business) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to add must be positive and non-zero.");
        }

        Item item = itemRepository.findByIdAndType_Business(itemId, business)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with ID: " + itemId));

        Stock stock = stockRepository.findByItem(item)
                .orElseThrow(() -> new ResourceNotFoundException("Stock entry not found for item."));

        stock.setQuantity(stock.getQuantity() + quantity);
        return stockMapper.stockToStockResponseDto(stockRepository.save(stock));
    }

    @Transactional
    public StockResponseDto subtractFromStock(Long itemId, int quantity, Business business) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to subtract must be positive and non-zero.");
        }

        Item item = itemRepository.findByIdAndType_Business(itemId, business)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with ID: " + itemId));

        Stock stock = stockRepository.findByItem(item)
                .orElseThrow(() -> new ResourceNotFoundException("Stock entry not found for item."));

        if (stock.getQuantity() < quantity) {
            throw new InsufficientStockException("Not enough stock available to subtract the requested quantity.");
        }

        stock.setQuantity(stock.getQuantity() - quantity);
        return stockMapper.stockToStockResponseDto(stockRepository.save(stock));
    }

    public Page<StockResponseDto> findAllFiltered(Business business, String itemName, String itemTypeName, int page, int size, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromOptionalString(sortDirection.toUpperCase()).orElse(Sort.Direction.ASC);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "item.name"));
        return stockRepository.findFilteredStocks(business, itemName, itemTypeName, pageable)
                .map(stockMapper::stockToStockResponseDto);
    }
}
