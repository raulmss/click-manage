package com.bezkoder.spring.inventory.repository;

import com.bezkoder.spring.inventory.model.Business;
import com.bezkoder.spring.inventory.model.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemTypeRepository extends JpaRepository<ItemType, Long> {

    List<ItemType> findByBusiness(Business business);

    Optional<ItemType> findByNameAndBusiness(String name, Business business);
}
