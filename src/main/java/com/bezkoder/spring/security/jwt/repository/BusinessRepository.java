package com.bezkoder.spring.security.jwt.repository;

import com.bezkoder.spring.inventory.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    // Find a business by name
    Optional<Business> findByName(String name);

    // Check if a business with the given name exists
    boolean existsByName(String name);
}
