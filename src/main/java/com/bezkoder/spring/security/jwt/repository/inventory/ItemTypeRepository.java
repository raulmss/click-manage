package com.bezkoder.spring.security.jwt.repository.inventory;

import com.bezkoder.spring.security.jwt.models.Business;
import com.bezkoder.spring.security.jwt.models.inventory.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemTypeRepository extends JpaRepository<ItemType, Long> {
    List<ItemType> findByBusiness(Business business);
}
