package com.web.pharma.inventory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_txn")
@Getter @Setter
public class InventoryTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="medicine_id")
    private Medicine medicine;

    private Long batchId;
    private Integer deltaUnits;
    private LocalDateTime occurredAt;
    private String reason;
    private String reference;
}
