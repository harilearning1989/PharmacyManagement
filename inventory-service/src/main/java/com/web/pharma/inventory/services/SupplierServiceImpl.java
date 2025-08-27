
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

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository repository;
    private final SupplierMapper mapper;

    @Transactional
    public SupplierDto create(SupplierDto dto) {
        log.info("Creating supplier name={}", dto.name());
        Supplier s = mapper.toEntity(dto);
        s.setId(null);
        var saved = repository.save(s);
        log.debug("Saved supplier id={}", saved.getId());
        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<SupplierDto> listAll() {
        log.debug("Listing suppliers");
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public SupplierDto findById(Long id) {
        log.debug("Finding supplier id={}", id);
        return repository.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found: " + id));
    }

    @Transactional
    public SupplierDto update(Long id, SupplierDto dto) {
        log.info("Updating supplier id={}", id);
        var existing = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Supplier not found: " + id));
        existing.setName(dto.name());
        existing.setPhone(dto.phone());
        existing.setGstin(dto.gstin());
        existing.setAddress(dto.address());
        var saved = repository.save(existing);
        log.debug("Updated supplier id={}", saved.getId());
        return mapper.toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting supplier id={}", id);
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Supplier not found: " + id);
        repository.deleteById(id);
    }
}
