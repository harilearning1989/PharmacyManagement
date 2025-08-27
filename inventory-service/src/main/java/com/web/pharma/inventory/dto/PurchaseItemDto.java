package com.web.pharma.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PurchaseItemDto(String medicineItemId, String batchNo, LocalDate expiryDate, Integer quantityUnits,
                              Integer packSizeUnits, BigDecimal mrp, BigDecimal purchasePrice, BigDecimal sellPrice) {
}

