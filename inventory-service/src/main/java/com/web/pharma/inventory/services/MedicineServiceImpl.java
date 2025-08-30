
package com.web.pharma.inventory.services;

import com.web.pharma.inventory.dto.MedicineDto;
import com.web.pharma.inventory.exception.ResourceNotFoundException;
import com.web.pharma.inventory.mapper.MedicineMapper;
import com.web.pharma.inventory.model.Medicine;
import com.web.pharma.inventory.repository.MedicineRepository;
import com.web.pharma.inventory.utils.JsonFileReaderUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    private static final Logger log = LoggerFactory.getLogger(MedicineServiceImpl.class);
    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;
    private final JsonFileReaderUtil jsonFileReaderUtil;

    @Override
    public int saveMedicines(MultipartFile file) {
        try {
            List<MedicineDto> medicines =
                    jsonFileReaderUtil.readListFromJson(file.getInputStream(), MedicineDto.class);
            var entities = medicines.stream().map(medicineMapper::toEntity).toList();
            medicineRepository.saveAll(entities);
            return entities.size();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineDto> getAllMedicines() {
        log.debug("Listing all medicines getAllMedicines");
        return medicineRepository.findAll().stream().map(medicineMapper::toDto).toList();
    }

    @Override
    @Transactional
    public MedicineDto create(MedicineDto dto) {
        log.info("Creating medicine itemId={}", dto.itemId());
        Medicine entity = medicineMapper.toEntity(dto);
        entity.setId(null);
        var saved = medicineRepository.save(entity);
        log.debug("Saved medicine id={}", saved.getId());
        return medicineMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineDto findByItemId(String itemId) {
        log.debug("Finding medicine by itemId={}", itemId);
        return medicineRepository.findByItemId(itemId).map(medicineMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found: " + itemId));
    }

}
