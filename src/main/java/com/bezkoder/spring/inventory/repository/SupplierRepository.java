package com.bezkoder.spring.inventory.repository;

import com.bezkoder.spring.inventory.model.Supplier;
import com.bezkoder.spring.security.jwt.models.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    boolean existsByNameIgnoreCaseAndBusiness(String name, Business business);

    boolean existsByTaxIdAndBusiness(String taxId, Business business);

    Optional<Supplier> findByIdAndBusiness(Long id, Business business);

    Page<Supplier> findAllByBusiness(Business business, Pageable pageable);

    Page<Supplier> findByBusinessAndNameContainingIgnoreCase(Business business, String name, Pageable pageable);

    @Query("""
    SELECT s FROM Supplier s
    WHERE s.business = :business
      AND (:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')))
    ORDER BY s.name ASC
""")
    Page<Supplier> filterSuppliers(Business business, String name, Pageable pageable);
}
