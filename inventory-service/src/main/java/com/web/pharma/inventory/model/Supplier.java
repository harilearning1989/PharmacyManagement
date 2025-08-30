package com.web.pharma.inventory.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "supplier")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
//@Builder(toBuilder = true)
public class Supplier extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUPPLIER_SEQ_GEN")
    @SequenceGenerator(name = "SUPPLIER_SEQ_GEN", sequenceName = "SUPPLIER_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "SUPPLIER_ID", nullable = false, unique = true, updatable = false)
    private Long supplierId;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;
    @Column(name = "PHONE", nullable = false, unique = true)
    private String phone;
    @Column(name = "GSTIN", nullable = false)
    private String gstin;

    // ✅ One-to-one relation with Address
    @OneToOne(mappedBy = "supplier", cascade = CascadeType.ALL, optional = false)
    private Address address;
}
