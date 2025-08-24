package com.web.pharma.customer.services;

import com.web.pharma.customer.entities.LoyaltyProgram;
import com.web.pharma.customer.entities.MedicalHistory;
import com.web.pharma.customer.entities.Patient;
import com.web.pharma.customer.entities.PrescriptionHistory;

import java.util.List;

public interface PatientService {
    Patient savePatient(Patient patient);

    Patient getPatient(Long id);

    MedicalHistory addMedicalHistory(Long id, MedicalHistory history);

    List<MedicalHistory> getMedicalHistories(Long id);

    PrescriptionHistory addPrescription(Long id, PrescriptionHistory prescription);

    List<PrescriptionHistory> getPrescriptions(Long id);

    LoyaltyProgram createOrUpdateLoyalty(Long id, int points, String status);

    LoyaltyProgram getLoyalty(Long id);

}
