package com.bezkoder.spring.inventory.repository;

import com.bezkoder.spring.inventory.model.ItemType;
import com.bezkoder.spring.security.jwt.models.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ItemTypeRepository extends JpaRepository<ItemType, Long> {

    // Paginated fetch of all types for a specific business
    Page<ItemType> findByBusiness(Business business, Pageable pageable);

    // Optional fetch by ID scoped to a business
    Optional<ItemType> findByIdAndBusiness(Long id, Business business);

    // Optional fetch by name scoped to a business (for validation)
    Optional<ItemType> findByNameAndBusiness(String name, Business business);

    // Check if a type name already exists for a business
    boolean existsByNameAndBusiness(String name, Business business);

    boolean existsByNameIgnoreCaseAndBusiness(String name, Business business);

    @Query("""
        SELECT it FROM ItemType it
        WHERE it.business = :business
          AND (:name IS NULL OR LOWER(it.name) LIKE LOWER(CONCAT('%', :name, '%')))
        ORDER BY it.name ASC
    """)
    Page<ItemType> filterItemTypes(Business business, String name, Pageable pageable);
}
