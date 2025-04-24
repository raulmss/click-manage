package com.bezkoder.spring.inventory.model;

import com.bezkoder.spring.security.jwt.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "item_exits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemExit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Item item;

    @Column(nullable = false)
    private int quantity;

    @Column(length = 50)
    private String lotNumber;

    @Column(length = 255)
    private String reason;

    @Column(nullable = false, updatable = false)
    private Instant exitDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;
}
