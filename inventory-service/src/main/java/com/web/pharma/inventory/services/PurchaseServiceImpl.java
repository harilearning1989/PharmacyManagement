
package com.web.pharma.inventory.services;

import com.web.pharma.inventory.dto.PurchaseDto;
import com.web.pharma.inventory.dto.PurchaseItemDto;
import com.web.pharma.inventory.exception.ResourceNotFoundException;
import com.web.pharma.inventory.model.InventoryBatch;
import com.web.pharma.inventory.model.InventoryTransaction;
import com.web.pharma.inventory.model.Purchase;
import com.web.pharma.inventory.model.PurchaseItem;
import com.web.pharma.inventory.repository.InventoryBatchRepository;
import com.web.pharma.inventory.repository.InventoryTransactionRepository;
import com.web.pharma.inventory.repository.MedicineRepository;
import com.web.pharma.inventory.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private static final Logger log = LoggerFactory.getLogger(PurchaseServiceImpl.class);

    private final PurchaseRepository purchaseRepository;
    private final MedicineRepository medicineRepository;
    private final InventoryBatchRepository batchRepository;
    private final InventoryTransactionRepository txnRepository;

    @Transactional
    public Long recordPurchase(PurchaseDto dto) {
        log.info("Recording purchase invoice={}", dto.invoiceNo());
        Purchase p = new Purchase();
        p.setInvoiceDate(dto.invoiceDate());
        p.setInvoiceNo(dto.invoiceNo());

        BigDecimal total = BigDecimal.ZERO;

        for (PurchaseItemDto it : dto.items()) {
            var med = medicineRepository.findByItemId(it.medicineItemId()).orElseThrow(() -> new ResourceNotFoundException("Medicine not found: " + it.medicineItemId()));
            PurchaseItem pi = new PurchaseItem();
            pi.setPurchase(p); pi.setMedicine(med); pi.setBatchNo(it.batchNo()); pi.setExpiryDate(it.expiryDate());
            pi.setQuantityUnits(it.quantityUnits()); pi.setPackSizeUnits(it.packSizeUnits()); pi.setMrp(it.mrp()); pi.setPurchasePrice(it.purchasePrice()); pi.setSellPrice(it.sellPrice());
            p.getItems().add(pi);

            InventoryBatch b = new InventoryBatch();
            b.setMedicine(med); b.setBatchNo(it.batchNo()); b.setExpiryDate(it.expiryDate()); b.setMrp(it.mrp()); b.setPurchasePrice(it.purchasePrice());
            b.setSellPrice(it.sellPrice()); b.setPackSize(it.packSizeUnits()); b.setTotalUnits(it.quantityUnits()); b.setAvailableUnits(it.quantityUnits());
            b.setCreatedAt(LocalDateTime.now());
            batchRepository.save(b);

            InventoryTransaction t = new InventoryTransaction();
            t.setMedicine(med); t.setBatchId(b.getId()); t.setDeltaUnits(it.quantityUnits()); t.setOccurredAt(LocalDateTime.now()); t.setReason("PURCHASE"); t.setReference(dto.invoiceNo());
            txnRepository.save(t);

            med.setQuantity((med.getQuantity() == null ? 0 : med.getQuantity()) + it.quantityUnits());
            medicineRepository.save(med);

            total = total.add(it.purchasePrice().multiply(BigDecimal.valueOf(it.quantityUnits())));
        }

        p.setTotalAmount(total);
        purchaseRepository.save(p);
        log.debug("Purchase saved id={}", p.getId());
        return p.getId();
    }
}
