package com.web.pharma.customer.entities;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "LOYALTY_PROGRAM")
@Data
public class LoyaltyProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int points;
    private String status;

    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
