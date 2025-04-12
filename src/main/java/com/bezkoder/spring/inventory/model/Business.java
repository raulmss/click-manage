package com.bezkoder.spring.inventory.model;

import java.util.List;

import com.bezkoder.spring.security.jwt.models.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "businesses")
@NoArgsConstructor
@Data
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String industry;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Address address;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users;

    public Business(String name, String industry, Address address) {
        this.name = name;
        this.industry = industry;
        this.address = address;
    }
}


