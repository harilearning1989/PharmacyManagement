package com.web.pharma.customer.services;

import com.web.pharma.customer.models.CustomerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<CustomerDto> getAllCustomers();
    CustomerDto create(CustomerDto dto);
    CustomerDto update(int id, CustomerDto dto);
    void delete(int id);
    Optional<CustomerDto> getById(int id);
    Page<CustomerDto> search(String q, Pageable pageable);
}
