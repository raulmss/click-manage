package com.bezkoder.spring.inventory.model;

import com.bezkoder.spring.security.jwt.models.Business;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    @JsonIgnore // if needed to avoid circular references
    private Business business;
}

