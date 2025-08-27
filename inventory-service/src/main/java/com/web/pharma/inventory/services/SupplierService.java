package com.web.pharma.inventory.services;

import com.web.pharma.inventory.dto.SupplierDto;
import jakarta.validation.Valid;

import java.util.List;

public interface SupplierService {
    SupplierDto create(@Valid SupplierDto dto);

    List<SupplierDto> listAll();

    SupplierDto findById(Long id);

    SupplierDto update(Long id, @Valid SupplierDto dto);

    void delete(Long id);
}
