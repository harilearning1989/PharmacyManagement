package com.web.pharma.inventory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "purchase_item")
@Getter @Setter
public class PurchaseItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="purchase_id")
    private Purchase purchase;

    @ManyToOne(optional=false)
    @JoinColumn(name="medicine_id")
    private Medicine medicine;

    private String batchNo;
    private LocalDate expiryDate;
    private Integer quantityUnits;
    private Integer packSizeUnits;
    private BigDecimal mrp;
    private BigDecimal purchasePrice;
    private BigDecimal sellPrice;
}
