package com.web.pharma.inventory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record MedicineDto(
        @JsonProperty("item_id") String itemId,
        @JsonProperty("item_type") String itemType,
        @JsonProperty("name") String name,
        @JsonProperty("strength_mg") Integer strengthMg,
        @JsonProperty("company") String company,
        @JsonProperty("expiry_date") String expiryDate,
        @JsonProperty("quantity") Integer quantity,
        @JsonProperty("unit_price") BigDecimal unitPrice,
        @JsonProperty("total_value") BigDecimal totalValue,
        @JsonProperty("created_by") Long createdBy,
        @JsonProperty("updated_by") Long updatedBy
) {
}
