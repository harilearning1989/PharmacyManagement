package com.web.pharma.inventory.dto;

public record AddressDto(
        Long id,
        String pinCode,
        String state,
        String district,
        String mandal,
        String post,
        String village,
        String fatherName
) {}

