package com.web.pharma.inventory.mapper;

import com.web.pharma.inventory.dto.SupplierDto;
import com.web.pharma.inventory.model.Address;
import com.web.pharma.inventory.model.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapperImpl implements SupplierMapper {
    @Override
    public SupplierDto toDto(Supplier s) {
        return null;
    }

    @Override
    public Supplier toEntity(SupplierDto dto) {
        Supplier supplier = Supplier.builder()
                .supplierId(dto.supplierId())
                .name(dto.name())
                .email(dto.email())
                .phone(dto.phone())
                .gstin(dto.gstin())
                .address(Address.builder()
                        .pinCode(dto.addressDto().pinCode())
                        .state(dto.addressDto().state())
                        .district(dto.addressDto().district())
                        .mandal(dto.addressDto().mandal())
                        .post(dto.addressDto().post())
                        .village(dto.addressDto().village())
                        .fatherName(dto.addressDto().fatherName())
                        .build())
                .build();

        supplier.getAddress().setSupplier(supplier); // ✅ set bidirectional link
        return supplier;
    }
}
