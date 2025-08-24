package com.web.pharma.customer.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PATIENTS")
@Data
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    private String phone;
    private String address;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<MedicalHistory> medicalHistories = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<PrescriptionHistory> prescriptions = new ArrayList<>();

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
    private LoyaltyProgram loyaltyProgram;
}
