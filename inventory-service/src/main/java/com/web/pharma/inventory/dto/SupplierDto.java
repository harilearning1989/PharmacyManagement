package com.web.pharma.inventory.dto;

/**
 * Supplier DTO
 */

public record SupplierDto(Long id, Long supplierId, String name, String email, String phone, String gstin,
                          AddressDto addressDto) {
}
