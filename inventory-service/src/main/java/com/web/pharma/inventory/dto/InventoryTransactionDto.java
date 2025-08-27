package com.web.pharma.inventory.dto;

import java.time.LocalDateTime;

public record InventoryTransactionDto(Long id, String medicineItemId, Long batchId, Integer deltaUnits, LocalDateTime occurredAt, String reason, String reference) {}
