package com.bezkoder.spring.inventory.repository;

import com.bezkoder.spring.inventory.model.Business;
import com.bezkoder.spring.inventory.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByBusiness(Business business);
    List<Item> findByBusinessAndNameContainingIgnoreCase(Business business, String name);

}