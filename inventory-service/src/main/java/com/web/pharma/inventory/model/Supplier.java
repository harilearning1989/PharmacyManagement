package com.web.pharma.inventory.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "supplier")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@SuperBuilder(toBuilder = true)
@Builder
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medicine_seq")
    @SequenceGenerator(name = "medicine_seq", sequenceName = "medicine_seq", allocationSize = 1)
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

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now(); // optional: keep same on insert
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ✅ One-to-one relation with Address
    @OneToOne(mappedBy = "supplier", cascade = CascadeType.ALL, optional = false)
    private Address address;
}
