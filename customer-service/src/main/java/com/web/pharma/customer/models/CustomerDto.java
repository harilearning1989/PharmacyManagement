package com.web.pharma.customer.models;

public record CustomerDto(
        int id,
        int custId,
        String name,
        String phone,
        String email,
        String gender,
        String dob,
        int createdBy,
        int updatedBy,
        String createdDate,
        String updatedDate
) {
}
