package com.web.pharma.inventory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "medicine")
@Getter
@Setter
public class Medicine {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id", nullable = false, unique = true)
    private String itemId;

    @Column(name = "item_type", nullable = false)
    private String itemType;

    @Column(nullable = false)
    private String name;

    @Column(name = "strength_mg")
    private Integer strengthMg;

    private String company;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    private Integer quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "total_value")
    private BigDecimal totalValue;
}
