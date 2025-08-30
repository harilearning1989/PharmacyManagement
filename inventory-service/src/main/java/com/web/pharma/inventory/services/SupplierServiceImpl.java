
package com.web.pharma.inventory.services;

import com.web.pharma.inventory.dto.SupplierDto;
import com.web.pharma.inventory.exception.ResourceNotFoundException;
import com.web.pharma.inventory.mapper.SupplierMapper;
import com.web.pharma.inventory.model.Supplier;
import com.web.pharma.inventory.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    @Transactional
    public SupplierDto saveSupplier(SupplierDto supplierDto) {
        log.info("Creating supplier name={}", supplierDto.name());
        Supplier supplier = supplierMapper.toEntity(supplierDto);
        supplier.setId(null);
        var saved = supplierRepository.save(supplier);
        log.debug("Saved supplier id={}", saved.getId());
        return supplierMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierDto> getAllSuppliers() {
        log.debug("Listing suppliers");
        return supplierRepository.findAll().stream().map(supplierMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SupplierDto> getSupplierById(Long id) {
        log.debug("Finding supplier id={}", id);
        return supplierRepository.findById(id).map(supplierMapper::toDto);
    }

    @Override
    @Transactional
    public SupplierDto updateSupplier(Long id, SupplierDto supplierDto) {
        log.info("Updating supplier id={}", id);
        var existing = supplierRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Supplier not found: " + id));
        existing.setName(supplierDto.name());
        existing.setPhone(supplierDto.phone());
        existing.setGstin(supplierDto.gstin());
        //existing.setAddress(supplierDto.addressDto());
        var saved = supplierRepository.save(existing);
        log.debug("Updated supplier id={}", saved.getId());
        return supplierMapper.toDto(saved);
    }

    @Override
    public SupplierDto patchSupplier(Long id, SupplierDto supplierDto) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Supplier with ID " + id + " not found"));

        Supplier updatedSupplier = Supplier.builder()
                .name(supplierDto.name() != null ? supplierDto.name() : supplier.getName())
                .email(supplierDto.email() != null ? supplierDto.email() : supplier.getEmail())
                .phone(supplierDto.phone() != null ? supplierDto.phone() : supplier.getPhone())
                //.address(supplierDto.address() != null ? supplierDto.address() : supplier.getAddress())
                .build();

        Supplier savedSupplier = supplierRepository.save(updatedSupplier);
        return supplierMapper.toDto(savedSupplier);
    }

    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        log.info("Deleting supplier id={}", id);
        if (!supplierRepository.existsById(id)) throw new ResourceNotFoundException("Supplier not found: " + id);
        supplierRepository.deleteById(id);
    }

}
