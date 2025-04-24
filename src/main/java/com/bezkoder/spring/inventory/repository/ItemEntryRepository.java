package com.bezkoder.spring.inventory.repository;

import com.bezkoder.spring.inventory.model.ItemEntry;
import com.bezkoder.spring.security.jwt.models.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemEntryRepository extends JpaRepository<ItemEntry, Long> {

    @Query("""
        SELECT e FROM ItemEntry e
        WHERE e.item.type.business = :business
          AND (:itemName IS NULL OR LOWER(e.item.name) LIKE LOWER(CONCAT('%', :itemName, '%')))
          AND (:supplierName IS NULL OR LOWER(e.supplier.name) LIKE LOWER(CONCAT('%', :supplierName, '%')))
          AND (:userName IS NULL OR LOWER(e.user.username) LIKE LOWER(CONCAT('%', :userName, '%')))
        ORDER BY e.entryDate DESC
    """)
    Page<ItemEntry> filterEntries(
            @Param("business") Business business,
            @Param("itemName") String itemName,
            @Param("supplierName") String supplierName,
            @Param("userName") String userName,
            Pageable pageable
    );
}
