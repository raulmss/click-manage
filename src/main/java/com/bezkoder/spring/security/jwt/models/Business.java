package com.bezkoder.spring.security.jwt.models;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "businesses")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String industry;
    private String address;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users;

    public Business() {}

    public Business(String name, String industry, String address) {
        this.name = name;
        this.industry = industry;
        this.address = address;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }
}


