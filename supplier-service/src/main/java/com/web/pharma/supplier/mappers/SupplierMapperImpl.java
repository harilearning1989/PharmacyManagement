package com.web.pharma.supplier.mappers;

import com.web.pharma.supplier.entities.Address;
import com.web.pharma.supplier.entities.Supplier;
import com.web.pharma.supplier.models.AddressDto;
import com.web.pharma.supplier.models.SupplierDto;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class SupplierMapperImpl implements SupplierMapper {

    @Override
    public Supplier toEntity(SupplierDto dto) {
        var builder = Supplier.builder()
                .supplierId(dto.supplierId())
                .name(dto.name())
                .email(dto.email())
                .phone(dto.phone())
                .createdBy(41)
                .updatedBy(41);

        return builder.build();
    }

    @Override
    public SupplierDto toDto(Supplier entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm:ss a", Locale.ENGLISH);
        String createdAt = entity.getCreatedAt().format(formatter);
        String updatedAt = entity.getUpdatedAt().format(formatter);
        Address address = entity.getAddress();

        AddressDto addressDto = new AddressDto(
                address.getId(), address.getFatherName(), address.getState(), address.getDistrict(), address.getMandal(),
                address.getPost(), address.getVillage(), address.getPinCode());
        return new SupplierDto(
                entity.getId(),
                entity.getSupplierId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getGstin(),
                addressDto,
                entity.getCreatedBy(),
                entity.getUpdatedBy(),
                createdAt,
                updatedAt
        );

    }

}
