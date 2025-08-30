package com.web.pharma.inventory.mapper;

import com.web.pharma.inventory.dto.MedicineDto;
import com.web.pharma.inventory.model.Medicine;
import com.web.pharma.inventory.model.Supplier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class MedicineMapperImpl implements MedicineMapper {

    public Medicine toEntity(MedicineDto dto) {
        var builder = Medicine.builder()
                .itemId(dto.itemId())
                .itemType(dto.itemType())
                .name(dto.name())
                .strengthMg(dto.strengthMg())
                .company(dto.company())
                .expiryDate(dto.expiryDate() != null && !dto.expiryDate().isBlank()
                        ? LocalDate.parse(dto.expiryDate(), DateTimeFormatter.ISO_DATE)
                        : null)
                .quantity(dto.quantity())
                .unitPrice(dto.unitPrice())
                .createdBy(1L)
                .updatedBy(dto.updatedBy());

        // 💡 Dynamic: calculate totalValue if both quantity & unitPrice exist
        if (dto.quantity() != null && dto.unitPrice() != null) {
            builder.totalValue(dto.unitPrice().multiply(BigDecimal.valueOf(dto.quantity())));
        } else {
            builder.totalValue(BigDecimal.ZERO);
        }

        // ✅ Set supplier only by ID (no need to fetch)
        if (dto.supplierId() != null) {
            builder.supplier(Supplier.builder().id(dto.supplierId()).build());
        }

        return builder.build();
    }


    public MedicineDto toDto(Medicine entity) {
        String expiryDateStr = entity.getExpiryDate() != null
                ? entity.getExpiryDate().format(DateTimeFormatter.ISO_DATE)
                : null;

        Long createdBy = (entity.getCreatedBy() != null) ? entity.getCreatedBy() : -1L;
        Long updatedBy = (entity.getUpdatedBy() != null) ? entity.getUpdatedBy() : -1L;

        return new MedicineDto(
                entity.getItemId(),
                entity.getItemType(),
                entity.getName(),
                entity.getStrengthMg(),
                entity.getCompany(),
                expiryDateStr,
                entity.getQuantity(),
                entity.getUnitPrice(),
                entity.getTotalValue(),
                createdBy,
                updatedBy,
                12L
        );
    }
}
