package com.web.pharma.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Medicine DTO record for API.
 */
public record MedicineDto(
    @NotBlank String itemId,
    @NotBlank String itemType,
    @NotBlank String name,
    Integer strengthMg,
    String company,
    LocalDate expiryDate,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal totalValue
) {}
