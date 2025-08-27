package com.web.pharma.inventory.dto;

import java.math.BigDecimal;
import java.util.List;

public record SaleDto(List<SaleItemDto> items, BigDecimal totalAmount) {}
