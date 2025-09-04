package com.web.pharma.customer.mappers;

import com.web.pharma.customer.entities.Customer;
import com.web.pharma.customer.models.CustomerDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer toEntity(CustomerDto dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dobLocalDate = LocalDate.parse(dto.dob(), formatter);
        var builder = Customer.builder()
                .custId(dto.custId())
                .name(dto.name())
                .gender(dto.gender())
                .email(dto.email())
                .phone(dto.phone())
                .dob(dobLocalDate)
                .createdBy(41)
                .updatedBy(41);

        return builder.build();
    }

    @Override
    public CustomerDto toDto(Customer entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm:ss a", Locale.ENGLISH);
        String createdAt = entity.getCreatedAt().format(formatter);
        String updatedAt = entity.getUpdatedAt().format(formatter);
        return new CustomerDto(
                entity.getId(),
                entity.getCustId(),
                entity.getName(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getGender(),
                entity.getDob().toString(),
                entity.getCreatedBy(),
                entity.getUpdatedBy(),
                createdAt,
                updatedAt
        );
    }

}
