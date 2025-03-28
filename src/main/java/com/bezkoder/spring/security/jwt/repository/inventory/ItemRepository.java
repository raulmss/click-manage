package com.bezkoder.spring.security.jwt.repository.inventory;

import com.bezkoder.spring.security.jwt.models.inventory.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByBusinessId(Long businessId);
    List<Item> findByBusinessIdAndNameContainingIgnoreCase(Long businessId, String name);
}