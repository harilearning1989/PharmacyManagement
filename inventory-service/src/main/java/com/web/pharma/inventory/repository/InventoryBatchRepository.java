package com.web.pharma.inventory.repository;

import com.web.pharma.inventory.model.InventoryBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface InventoryBatchRepository extends JpaRepository<InventoryBatch, Long> {

    @Query("select b from InventoryBatch b where b.medicine.id = :medicineId and b.availableUnits > 0 and b.expiryDate >= :today order by b.expiryDate asc, b.id asc")
    List<InventoryBatch> findSellableBatches(@Param("medicineId") Long medicineId, @Param("today") LocalDate today);
}
