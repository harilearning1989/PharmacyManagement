
package com.web.pharma.inventory.services;

import com.web.pharma.inventory.dto.SaleDto;
import com.web.pharma.inventory.dto.SaleItemDto;
import com.web.pharma.inventory.exception.BusinessException;
import com.web.pharma.inventory.exception.ResourceNotFoundException;
import com.web.pharma.inventory.model.InventoryBatch;
import com.web.pharma.inventory.model.InventoryTransaction;
import com.web.pharma.inventory.model.Sale;
import com.web.pharma.inventory.model.SaleItem;
import com.web.pharma.inventory.repository.InventoryBatchRepository;
import com.web.pharma.inventory.repository.InventoryTransactionRepository;
import com.web.pharma.inventory.repository.MedicineRepository;
import com.web.pharma.inventory.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private static final Logger log = LoggerFactory.getLogger(SaleServiceImpl.class);

    private final SaleRepository saleRepository;
    private final MedicineRepository medicineRepository;
    private final InventoryBatchRepository batchRepository;
    private final InventoryTransactionRepository txnRepository;

    @Transactional
    public Long createSale(SaleDto dto) {
        log.info("Creating sale with {} items", dto.items().size());
        Sale sale = new Sale();
        sale.setCreatedAt(LocalDateTime.now());
        BigDecimal grand = BigDecimal.ZERO;

        for (SaleItemDto it : dto.items()) {
            var med = medicineRepository.findByItemId(it.medicineItemId()).orElseThrow(() -> new ResourceNotFoundException("Medicine not found: " + it.medicineItemId()));
            int remaining = it.quantityUnits();
            List<InventoryBatch> batches = batchRepository.findSellableBatches(med.getId(), LocalDate.now());
            for (InventoryBatch b : batches) {
                if (remaining == 0) break;
                int can = Math.min(remaining, b.getAvailableUnits() == null ? 0 : b.getAvailableUnits());
                if (can <= 0) continue;
                b.setAvailableUnits(b.getAvailableUnits() - can);
                batchRepository.save(b);

                SaleItem si = new SaleItem();
                si.setSale(sale); si.setMedicine(med); si.setBatch(b); si.setQuantityUnits(can); si.setUnitPrice(b.getSellPrice());
                sale.getItems().add(si);

                InventoryTransaction t = new InventoryTransaction();
                t.setMedicine(med); t.setBatchId(b.getId()); t.setDeltaUnits(-can); t.setOccurredAt(LocalDateTime.now()); t.setReason("SALE");
                txnRepository.save(t);

                grand = grand.add(b.getSellPrice().multiply(BigDecimal.valueOf(can)));
                remaining -= can;
            }
            if (remaining > 0) throw new BusinessException("Insufficient stock for item: " + it.medicineItemId());
            med.setQuantity((med.getQuantity() == null ? 0 : med.getQuantity()) - it.quantityUnits());
            medicineRepository.save(med);
        }

        sale.setTotalAmount(grand);
        saleRepository.save(sale);
        log.debug("Sale saved id={}", sale.getId());
        return sale.getId();
    }
}
