package com.web.pharma.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Inventory Batch DTO.
 */
public record InventoryBatchDto(
    Long id,
    String medicineItemId,
    String batchNo,
    LocalDate expiryDate,
    BigDecimal mrp,
    BigDecimal purchasePrice,
    BigDecimal sellPrice,
    Integer packSize,
    Integer totalUnits,
    Integer availableUnits
) {}
