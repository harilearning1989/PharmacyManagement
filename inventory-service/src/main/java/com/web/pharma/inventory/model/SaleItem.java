package com.web.pharma.inventory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "sale_item")
@Getter @Setter
public class SaleItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="sale_id")
    private Sale sale;

    @ManyToOne(optional=false)
    @JoinColumn(name="medicine_id")
    private Medicine medicine;

    @ManyToOne(optional=false)
    @JoinColumn(name="batch_id")
    private InventoryBatch batch;

    private Integer quantityUnits;
    private BigDecimal unitPrice;
}
