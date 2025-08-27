package com.web.pharma.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PurchaseDto(Long supplierId, LocalDate invoiceDate, String invoiceNo, List<PurchaseItemDto> items, BigDecimal totalAmount) {}
