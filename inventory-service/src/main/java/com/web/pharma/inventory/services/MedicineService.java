package com.web.pharma.inventory.services;

import com.web.pharma.inventory.dto.MedicineDto;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MedicineService {
    MedicineDto create(@Valid MedicineDto dto);

    MedicineDto findByItemId(String itemId);

    int saveMedicines(MultipartFile file);

    List<MedicineDto> getAllMedicines();
}
