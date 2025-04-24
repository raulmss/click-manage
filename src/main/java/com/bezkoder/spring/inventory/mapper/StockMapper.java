package com.bezkoder.spring.inventory.mapper;

import com.bezkoder.spring.inventory.dto.request.StockRequestDto;
import com.bezkoder.spring.inventory.dto.response.StockResponseDto;
import com.bezkoder.spring.inventory.model.Item;
import com.bezkoder.spring.inventory.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockMapper {

    private final ItemMapper itemMapper;

    public Stock stockRequestDtoToStock(StockRequestDto dto, Item item) {
        if (dto == null || item == null) return null;

        Stock stock = new Stock();
        stock.setItem(item);
        stock.setQuantity(0); // Default to 0 on creation
        stock.setMinThreshold(dto.minThreshold());
        return stock;
    }

    public StockResponseDto stockToStockResponseDto(Stock stock) {
        if (stock == null) return null;

        return new StockResponseDto(
                stock.getId(),
                itemMapper.itemToItemResponseDto(stock.getItem()),
                stock.getQuantity(),
                stock.getMinThreshold()
        );
    }
}

