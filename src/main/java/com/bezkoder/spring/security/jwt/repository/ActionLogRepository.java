// Create ActionLogRepository.java
package com.bezkoder.spring.security.jwt.repository;

import com.bezkoder.spring.security.jwt.models.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
}