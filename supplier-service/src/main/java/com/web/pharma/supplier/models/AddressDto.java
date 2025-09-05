package com.web.pharma.supplier.models;

public record AddressDto(
        int id,
        String fatherName,
        String state,
        String district,
        String mandal,
        String post,
        String village,
        String pinCode
) {
}
