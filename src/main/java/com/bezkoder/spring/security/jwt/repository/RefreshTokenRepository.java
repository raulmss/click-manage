package com.bezkoder.spring.security.jwt.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bezkoder.spring.security.jwt.models.RefreshToken;
import com.bezkoder.spring.security.jwt.models.User;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  @Modifying
  int deleteByUser(User user);

  @Modifying
  @Transactional
  @Query(value = "DELETE FROM refreshtoken WHERE user_id = :userId", nativeQuery = true)
  void deleteByUserId(@Param("userId") Long userId);

}
