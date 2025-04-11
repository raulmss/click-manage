package com.bezkoder.spring.inventory.repository;

import com.bezkoder.spring.security.jwt.models.Business;
import com.bezkoder.spring.inventory.model.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemTypeRepository extends JpaRepository<ItemType, Long> {
    List<ItemType> findByBusiness(Business business);
}
