package com.web.pharma.auth.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "ACTIVITY_LOGS")
@Data
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String action;
    private LocalDateTime timestamp = LocalDateTime.now();
}
