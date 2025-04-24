package com.bezkoder.spring.inventory.repository;

import com.bezkoder.spring.inventory.model.ItemExit;
import com.bezkoder.spring.security.jwt.models.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemExitRepository extends JpaRepository<ItemExit, Long> {

    @Query("""
        SELECT e FROM ItemExit e
        WHERE e.item.type.business = :business
          AND (:itemName IS NULL OR LOWER(e.item.name) LIKE LOWER(CONCAT('%', :itemName, '%')))
          AND (:userName IS NULL OR LOWER(e.user.username) LIKE LOWER(CONCAT('%', :userName, '%')))
        ORDER BY e.exitDate DESC
    """)
    Page<ItemExit> filterItemExits(
            @Param("itemName") String itemName,
            @Param("userName") String userName,
            @Param("business") Business business,
            Pageable pageable
    );

    Optional<ItemExit> findByIdAndItemTypeBusiness(Long id, Business business);
}
