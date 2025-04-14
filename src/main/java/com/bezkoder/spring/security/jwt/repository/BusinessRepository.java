package com.bezkoder.spring.security.jwt.repository;

import com.bezkoder.spring.security.jwt.models.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    // Find a business by name
    Optional<Business> findByName(String name);

    // Check if a business with the given name exists
    boolean existsByName(String name);


    @Query("""
    SELECT b FROM Business b
    WHERE (:name IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:industry IS NULL OR LOWER(b.industry) LIKE LOWER(CONCAT('%', :industry, '%')))
      AND (:city IS NULL OR LOWER(b.address.city) LIKE LOWER(CONCAT('%', :city, '%')))
      AND (:state IS NULL OR LOWER(b.address.state) LIKE LOWER(CONCAT('%', :state, '%')))
    ORDER BY b.name ASC
""")
    Page<Business> filterBusinesses(
            @Param("name") String name,
            @Param("industry") String industry,
            @Param("city") String city,
            @Param("state") String state,
            Pageable pageable
    );
}
