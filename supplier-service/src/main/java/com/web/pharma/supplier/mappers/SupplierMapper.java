package com.web.pharma.supplier.mappers;


import com.web.pharma.supplier.entities.Supplier;
import com.web.pharma.supplier.models.SupplierDto;

public interface SupplierMapper {
    SupplierDto toDto(Supplier customer);

    Supplier toEntity(SupplierDto dto);
}
