package com.web.pharma.supplier.services;

import com.web.pharma.supplier.models.SupplierDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SupplierService {
    List<SupplierDto> getAllSuppliers();

    SupplierDto create(SupplierDto dto);

    SupplierDto update(int id, SupplierDto dto);

    void delete(int id);

    Optional<SupplierDto> getById(int id);

    Page<SupplierDto> search(String q, Pageable pageable);

    int saveSupplier(MultipartFile file);

    Optional<SupplierDto> patchSupplier(int id, Map<String, Object> updates);
}
