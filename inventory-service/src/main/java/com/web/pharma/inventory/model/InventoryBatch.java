package com.web.pharma.inventory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_batch")
@Getter @Setter
public class InventoryBatch {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="medicine_id")
    private Medicine medicine;

    @Column(name="batch_no", nullable=false)
    private String batchNo;

    @Column(name="expiry_date", nullable=false)
    private LocalDate expiryDate;

    private BigDecimal mrp;
    private BigDecimal purchasePrice;
    private BigDecimal sellPrice;
    private Integer packSize;
    private Integer totalUnits;
    private Integer availableUnits;
    private LocalDateTime createdAt;
}
