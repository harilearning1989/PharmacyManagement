package com.web.pharma.customer.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "Customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder   // 👈 instead of @Builder
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMER_ID_SEQ_GEN")
    @SequenceGenerator(name = "CUSTOMER_ID_SEQ_GEN", sequenceName = "CUSTOMER_ID_SEQ", allocationSize = 1)
    private int id;

    @Column(name = "CUST_ID", nullable = false, unique = true)
    private int custId;

    @Column(name = "NAME", nullable = false, length = 200)
    private String name;

    @Column(name = "PHONE", nullable = false, length = 30)
    private String phone;

    @Column(name = "EMAIL", nullable = false, length = 150)
    private String email;

    @Column(name = "GENDER", nullable = false, length = 20)
    private String gender;

    @Column(name = "DOB", nullable = false)
    private LocalDate dob; // LocalDate for date-only (no time)

}
