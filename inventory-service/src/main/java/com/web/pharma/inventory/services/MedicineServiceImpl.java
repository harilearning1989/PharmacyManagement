
package com.web.pharma.inventory.services;

import com.web.pharma.inventory.dto.MedicineDto;
import com.web.pharma.inventory.exception.ResourceNotFoundException;
import com.web.pharma.inventory.mapper.MedicineMapper;
import com.web.pharma.inventory.model.Medicine;
import com.web.pharma.inventory.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    private static final Logger log = LoggerFactory.getLogger(MedicineServiceImpl.class);
    private final MedicineRepository medicineRepository;
    private final MedicineMapper mapper;

    @Override
    @Transactional
    public MedicineDto create(MedicineDto dto) {
        log.info("Creating medicine itemId={}", dto.itemId());
        Medicine entity = mapper.toEntity(dto);
        entity.setId(null);
        var saved = medicineRepository.save(entity);
        log.debug("Saved medicine id={}", saved.getId());
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineDto> listAll() {
        log.debug("Listing all medicines");
        return medicineRepository.findAll().stream().map(mapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineDto findByItemId(String itemId) {
        log.debug("Finding medicine by itemId={}", itemId);
        return medicineRepository.findByItemId(itemId).map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found: " + itemId));
    }
}
