package com.web.pharma.inventory.mapper;

import com.web.pharma.inventory.dto.MedicineDto;
import com.web.pharma.inventory.model.Medicine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * MapStruct mapper for Medicine.
 */
@Mapper(componentModel = "spring")
public interface MedicineMapper {

    MedicineDto toDto(Medicine entity);

    @Mapping(target="id", ignore=true)
    Medicine toEntity(MedicineDto dto);
}
