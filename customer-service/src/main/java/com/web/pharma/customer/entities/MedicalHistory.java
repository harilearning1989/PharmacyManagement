package com.web.pharma.customer.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "MEDICAL_HISTORY")
@Data
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String diagnosis;
    private String treatment;
    private LocalDateTime dateOfVisit;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
