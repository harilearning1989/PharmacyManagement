package com.web.pharma.customer.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "PRESCRIPTION_HISTORY")
@Data
public class PrescriptionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medicineName;
    private String dosage;
    private LocalDateTime prescribedDate;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
