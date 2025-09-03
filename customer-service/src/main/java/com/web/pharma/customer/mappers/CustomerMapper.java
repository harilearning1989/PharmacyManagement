package com.web.pharma.customer.mappers;

import com.web.pharma.customer.entities.Customer;
import com.web.pharma.customer.models.CustomerDto;

public interface CustomerMapper {
    CustomerDto toDto(Customer customer);
    Customer toEntity(CustomerDto dto);
}
