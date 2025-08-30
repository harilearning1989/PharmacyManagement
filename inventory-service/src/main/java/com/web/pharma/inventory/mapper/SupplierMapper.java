package com.web.pharma.inventory.mapper;

import com.web.pharma.inventory.dto.SupplierDto;
import com.web.pharma.inventory.model.Supplier;

public interface SupplierMapper {
    SupplierDto toDto(Supplier s);

    Supplier toEntity(SupplierDto dto);
}
