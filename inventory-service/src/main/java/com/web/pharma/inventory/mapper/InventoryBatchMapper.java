package com.web.pharma.inventory.mapper;

import com.web.pharma.inventory.dto.InventoryBatchDto;
import com.web.pharma.inventory.model.InventoryBatch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Inventory batch mapper.
 */
@Mapper(componentModel = "spring")
public interface InventoryBatchMapper {
    InventoryBatchDto toDto(InventoryBatch b);
}
