package com.web.pharma.supplier.models;

public record SupplierDto(
        int id,
        int supplierId,
        String name,
        String email,
        String phone,
        String gstin,
        AddressDto addressDto,
        int createdBy,
        int updatedBy,
        String createdDate,
        String updatedDate
) {
}
