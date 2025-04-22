package com.bezkoder.spring.inventory.repository;

import com.bezkoder.spring.inventory.model.Item;
import com.bezkoder.spring.inventory.model.ItemType;
import com.bezkoder.spring.security.jwt.models.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("""
    SELECT i FROM Item i
    WHERE i.type.business = :business
      AND (:name IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:barCode IS NULL OR LOWER(i.barCode) LIKE LOWER(CONCAT('%', :barCode, '%')))
      AND (:itemTypeName IS NULL OR LOWER(i.type.name) LIKE LOWER(CONCAT('%', :itemTypeName, '%')))
    ORDER BY i.name ASC
""")
    Page<Item> filterItems(@Param("business") Business business,
                           @Param("name") String name,
                           @Param("barCode") String barCode,
                           @Param("itemTypeName") String itemTypeName,
                           Pageable pageable);




    // Filter by Business and paginate
    Page<Item> findByType_Business(Business business, Pageable pageable);

    // Search by name (or partial match), within a business context
    Page<Item> findByType_BusinessAndNameContainingIgnoreCase(Business business, String name, Pageable pageable);

    // Get by ID ensuring it's under a specific business
    Optional<Item> findByIdAndType_Business(Long id, Business business);

    boolean existsByBarCodeIgnoreCaseAndType_Business(String barCode, Business business);
    boolean existsByNameIgnoreCaseAndType_Business(String name, Business business);
}
