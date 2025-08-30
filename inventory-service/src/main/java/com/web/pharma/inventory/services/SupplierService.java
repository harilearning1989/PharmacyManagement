package com.web.pharma.inventory.services;

import com.web.pharma.inventory.dto.SupplierDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SupplierService {

    SupplierDto saveSupplier(SupplierDto supplier);

    List<SupplierDto> getAllSuppliers();

    Optional<SupplierDto> getSupplierById(Long id);

    SupplierDto updateSupplier(Long id, SupplierDto supplier);

    SupplierDto patchSupplier(Long id, SupplierDto supplierDto);

    void deleteSupplier(Long id);
}
