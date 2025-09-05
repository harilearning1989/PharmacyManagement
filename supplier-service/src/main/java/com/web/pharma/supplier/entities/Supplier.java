package com.web.pharma.supplier.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "SUPPLIER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder   // 👈 instead of @Builder
public class Supplier extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUPPLIER_SEQ_GEN")
    @SequenceGenerator(name = "SUPPLIER_SEQ_GEN", sequenceName = "SUPPLIER_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name = "SUPPLIER_ID", nullable = false, unique = true, updatable = false)
    private int supplierId;
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
