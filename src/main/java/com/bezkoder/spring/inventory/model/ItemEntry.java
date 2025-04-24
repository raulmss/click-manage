package com.bezkoder.spring.inventory.model;

import com.bezkoder.spring.security.jwt.models.Business;
import com.bezkoder.spring.security.jwt.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "item_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private Instant entryDate;

    private LocalDate expiryDate;

    private String lotNumber;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
