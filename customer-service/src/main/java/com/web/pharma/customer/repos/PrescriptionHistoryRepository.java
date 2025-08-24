package com.web.pharma.customer.repos;

import com.web.pharma.customer.entities.PrescriptionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionHistoryRepository extends JpaRepository<PrescriptionHistory, Long> {
    List<PrescriptionHistory> findByPatientId(Long patientId);
}
