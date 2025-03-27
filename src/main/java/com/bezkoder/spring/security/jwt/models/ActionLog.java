// Create ActionLog.java
package com.bezkoder.spring.security.jwt.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ActionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String action;
    private LocalDateTime timestamp;
    private String requestInfo;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getRequestInfo() { return requestInfo; }
    public void setRequestInfo(String requestInfo) { this.requestInfo = requestInfo; }
}