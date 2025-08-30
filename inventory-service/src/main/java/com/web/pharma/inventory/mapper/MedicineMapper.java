package com.web.pharma.inventory.mapper;

import com.web.pharma.inventory.dto.MedicineDto;
import com.web.pharma.inventory.model.Medicine;

public interface MedicineMapper {
    Medicine toEntity(MedicineDto dto);
    MedicineDto toDto(Medicine medicine);
}
