package com.bezkoder.spring.security.jwt.repository;

import java.util.Optional;

import com.bezkoder.spring.security.jwt.models.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bezkoder.spring.security.jwt.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  @Query("""
    SELECT u FROM User u
    JOIN u.roles r
    WHERE u.business = :business
    AND (:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')))
    AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
    AND (:role IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :role, '%')))
""")
  Page<User> filterUsers(
          @Param("business") Business business,
          @Param("username") String username,
          @Param("email") String email,
          @Param("role") String role,
          Pageable pageable
  );

  @Query("""
        SELECT u FROM User u
        WHERE (:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')))
          AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
          AND (:role IS NULL OR EXISTS (
                SELECT r FROM u.roles r 
                WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :role, '%'))
          ))
          AND (:businessName IS NULL OR LOWER(u.business.name) LIKE LOWER(CONCAT('%', :businessName, '%')))
    """)
  Page<User> filterAllUsersForAdmin(
          @Param("username") String username,
          @Param("email") String email,
          @Param("role") String role,
          @Param("businessName") String businessName,
          Pageable pageable
  );

  Optional<User> findByUsernameAndBusiness(String username, Business business);

}
