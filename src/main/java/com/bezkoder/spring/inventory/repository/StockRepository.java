package com.bezkoder.spring.inventory.repository;

import com.bezkoder.spring.inventory.model.Item;
import com.bezkoder.spring.inventory.model.Stock;
import com.bezkoder.spring.security.jwt.models.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByItem(Item item);

    @Query("""
        SELECT s FROM Stock s
        WHERE s.item = :item AND s.item.type.business = :business
    """)
    Optional<Stock> findByItemAndBusiness(@Param("item") Item item, @Param("business") Business business);

    Optional<Stock> findByIdAndItem_Type_Business(Long id, Business business);
    boolean existsByItem(Item item);

    @Query("""
    SELECT s FROM Stock s
    WHERE s.item.type.business = :business
      AND (:itemName IS NULL OR LOWER(s.item.name) LIKE LOWER(CONCAT('%', :itemName, '%')))
      AND (:itemTypeName IS NULL OR LOWER(s.item.type.name) LIKE LOWER(CONCAT('%', :itemTypeName, '%')))
""")
    Page<Stock> findFilteredStocks(
            @Param("business") Business business,
            @Param("itemName") String itemName,
            @Param("itemTypeName") String itemTypeName,
            Pageable pageable
    );
}
