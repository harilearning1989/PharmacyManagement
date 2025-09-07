package com.web.pharma.customer.services;

import com.web.pharma.customer.entities.Customer;
import com.web.pharma.customer.mappers.CustomerMapper;
import com.web.pharma.customer.models.CustomerDto;
import com.web.pharma.customer.repos.CustomerRepository;
import com.web.pharma.customer.utils.JsonFileReaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final JsonFileReaderUtil jsonFileReaderUtil;

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        log.debug("Listing all customers getAllCustomers");
        return customerRepository.findAll().stream().map(customerMapper::toDto).toList();
    }

    @Override
    public CustomerDto create(CustomerDto dto) {
        log.info("Creating customer with custId={}", dto.custId());
        Customer saved = customerRepository.save(customerMapper.toEntity(dto));
        return customerMapper.toDto(saved);
    }

    @Override
    public CustomerDto update(int id, CustomerDto dto) {
        log.info("Updating customer id={}", id);
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found id=" + id));
        existing.setName(dto.name());
        existing.setPhone(dto.phone());
        existing.setEmail(dto.email());
        existing.setGender(dto.gender());
        //existing.setDob(dto.dob());
        existing.setUpdatedBy(dto.updatedBy());
        Customer saved = customerRepository.save(existing);
        return customerMapper.toDto(saved);
    }

    @Override
    @Transactional
    public Optional<CustomerDto> patchCustomer(int id, Map<String, Object> updates) {
        return customerRepository.findById(id).map(entity -> {
            updates.forEach((field, value) -> {
                switch (field) {
                    case "name" -> entity.setName((String) value);
                    case "phone" -> entity.setPhone((String) value);
                    case "email" -> entity.setEmail((String) value);
                    case "gender" -> entity.setGender((String) value);
                    case "dob" -> {
                        LocalDate dobLocalDate = LocalDate.parse((String) value, formatter);
                        entity.setDob(dobLocalDate);
                    }
                    default -> throw new IllegalArgumentException("Field '" + field + "' is not updatable");
                }
            });
            Customer saved = customerRepository.save(entity);
            return customerMapper.toDto(saved);
        });
    }

    @Override
    public Optional<CustomerDto> findByPhone(String phone) {
        Customer customer = customerRepository.findByPhone(phone);
        return Optional.ofNullable(customerMapper.toDto(customer));
    }


    @Override
    public void delete(int id) {
        log.info("Deleting customer id={}", id);
        customerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerDto> getById(int id) {
        return customerRepository.findById(id).map(customerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDto> search(String q, Pageable pageable) {
        log.debug("Searching customers q={}", q);
        return customerRepository.search(q, pageable).map(customerMapper::toDto);
    }

    @Override
    public int saveCustomer(MultipartFile file) {
        try {
            List<CustomerDto> medicines =
                    jsonFileReaderUtil.readListFromJson(file.getInputStream(), CustomerDto.class);
            var entities = medicines.stream().map(customerMapper::toEntity).toList();
            customerRepository.saveAll(entities);
            return entities.size();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
