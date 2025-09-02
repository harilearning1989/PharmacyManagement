package com.web.pharma.inventory.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.math.BigDecimal;

public record MedicineDto(
        @JsonAlias("item_id") String itemId,
        @JsonAlias("item_type") String itemType,
        String name,
        @JsonAlias("strength_mg") Integer strengthMg,
        String company,
        @JsonAlias("expiry_date") String expiryDate,
        Integer quantity,
        @JsonAlias("unit_price") BigDecimal unitPrice,
        @JsonAlias("total_value") BigDecimal totalValue,
        @JsonAlias("created_by") Long createdBy,
        @JsonAlias("updated_by") Long updatedBy,
        @JsonAlias("supplier_id") Long supplierId
) {
}

/*public record MedicineDto(
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
        @JsonProperty("updated_by") Long updatedBy,
        @JsonProperty("supplierId") Long supplierId
) {
}*/
