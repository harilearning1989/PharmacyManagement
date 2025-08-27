package com.web.pharma.inventory.dto;

import java.math.BigDecimal;

public record SaleItemDto(String medicineItemId, Integer quantityUnits, BigDecimal unitPrice) {
}
