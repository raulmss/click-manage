package com.bezkoder.spring.inventory.model;

import com.bezkoder.spring.security.jwt.models.Address;
import com.bezkoder.spring.security.jwt.models.Business;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "suppliers", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "business_id"}),
        @UniqueConstraint(columnNames = "taxId")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String email;

    private String phone;

    private String taxId; //in Brazil, known as CNPJ

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    private String contactPerson;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Business business;
}
