package com.web.pharma.inventory.services;

import com.web.pharma.inventory.dto.MedicineDto;
import jakarta.validation.Valid;

import java.util.List;

public interface MedicineService {
    MedicineDto create(@Valid MedicineDto dto);

    List<MedicineDto> listAll();

    MedicineDto findByItemId(String itemId);
}
