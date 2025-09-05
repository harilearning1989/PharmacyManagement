package com.web.pharma.supplier.services;

import com.web.pharma.supplier.entities.Supplier;
import com.web.pharma.supplier.mappers.SupplierMapper;
import com.web.pharma.supplier.models.SupplierDto;
import com.web.pharma.supplier.repos.SupplierRepository;
import com.web.pharma.supplier.utils.JsonFileReaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository customerRepository;
    private final SupplierMapper customerMapper;
    private final JsonFileReaderUtil jsonFileReaderUtil;

    @Override
    @Transactional(readOnly = true)
    public List<SupplierDto> getAllSuppliers() {
        log.debug("Listing all customers getAllSuppliers");
        return customerRepository.findAll().stream().map(customerMapper::toDto).toList();
    }

    @Override
    public SupplierDto create(SupplierDto dto) {
        log.info("Creating customer with custId={}", dto.supplierId());
        Supplier saved = customerRepository.save(customerMapper.toEntity(dto));
        return customerMapper.toDto(saved);
    }

    @Override
    public SupplierDto update(int id, SupplierDto dto) {
        log.info("Updating customer id={}", id);
        Supplier existing = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found id=" + id));
        existing.setName(dto.name());
        existing.setPhone(dto.phone());
        existing.setEmail(dto.email());
        //existing.setDob(dto.dob());
        existing.setUpdatedBy(dto.updatedBy());
        Supplier saved = customerRepository.save(existing);
        return customerMapper.toDto(saved);
    }

    @Override
    @Transactional
    public Optional<SupplierDto> patchSupplier(int id, Map<String, Object> updates) {
        return customerRepository.findById(id).map(entity -> {
            updates.forEach((field, value) -> {
                switch (field) {
                    case "name" -> entity.setName((String) value);
                    case "phone" -> entity.setPhone((String) value);
                    case "email" -> entity.setEmail((String) value);
                    default -> throw new IllegalArgumentException("Field '" + field + "' is not updatable");
                }
            });
            Supplier saved = customerRepository.save(entity);
            return customerMapper.toDto(saved);
        });
    }


    @Override
    public void delete(int id) {
        log.info("Deleting customer id={}", id);
        customerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SupplierDto> getById(int id) {
        return customerRepository.findById(id).map(customerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierDto> search(String q, Pageable pageable) {
        log.debug("Searching customers q={}", q);
        return customerRepository.search(q, pageable).map(customerMapper::toDto);
    }

    @Override
    public int saveSupplier(MultipartFile file) {
        try {
            List<SupplierDto> medicines =
                    jsonFileReaderUtil.readListFromJson(file.getInputStream(), SupplierDto.class);
            var entities = medicines.stream().map(customerMapper::toEntity).toList();
            customerRepository.saveAll(entities);
            return entities.size();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
