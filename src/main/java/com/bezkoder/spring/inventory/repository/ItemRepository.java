package com.bezkoder.spring.inventory.repository;

import com.bezkoder.spring.inventory.model.Item;
import com.bezkoder.spring.inventory.model.ItemType;
import com.bezkoder.spring.security.jwt.models.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // Filter by Business and paginate
    Page<Item> findByType_Business(Business business, Pageable pageable);

    // Search by name (or partial match), within a business context
    Page<Item> findByType_BusinessAndNameContainingIgnoreCase(Business business, String name, Pageable pageable);

    // Get by ID ensuring it's under a specific business
    Optional<Item> findByIdAndType_Business(Long id, Business business);
}
