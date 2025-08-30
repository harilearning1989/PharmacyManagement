package com.web.pharma.inventory.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ADDRESS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADDRESS_SEQ_GEN")
    @SequenceGenerator(name = "ADDRESS_SEQ_GEN", sequenceName = "ADDRESS_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "FATHER_NAME")
    private String fatherName;

    @Column(name = "STATE", nullable = false)
    private String state;

    @Column(name = "DISTRICT", nullable = false)
    private String district;

    @Column(name = "MANDAL", nullable = false)
    private String mandal;

    @Column(name = "POST", nullable = false)
    private String post;

    @Column(name = "VILLAGE", nullable = false)
    private String village;

    @Column(name = "PIN_CODE", nullable = false)
    private String pinCode;

    @OneToOne
    @JoinColumn(name = "SUPPLIER_ID", referencedColumnName = "SUPPLIER_ID", nullable = false, unique = true)
    private Supplier supplier;

}

