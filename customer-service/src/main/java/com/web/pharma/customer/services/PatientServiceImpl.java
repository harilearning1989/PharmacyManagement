package com.web.pharma.customer.services;

import com.web.pharma.customer.entities.LoyaltyProgram;
import com.web.pharma.customer.entities.MedicalHistory;
import com.web.pharma.customer.entities.Patient;
import com.web.pharma.customer.entities.PrescriptionHistory;
import com.web.pharma.customer.repos.LoyaltyProgramRepository;
import com.web.pharma.customer.repos.MedicalHistoryRepository;
import com.web.pharma.customer.repos.PatientRepository;
import com.web.pharma.customer.repos.PrescriptionHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final PrescriptionHistoryRepository prescriptionRepository;
    private final LoyaltyProgramRepository loyaltyRepository;

    // Create or update patient
    @Override
    public Patient savePatient(Patient patient) {
        Patient saved = patientRepository.save(patient);
        log.info("created/updated patient [{}]", saved.getId());
        return saved;
    }

    // Add medical history
    @Override
    public MedicalHistory addMedicalHistory(Long patientId, MedicalHistory history) {
        Patient patient = getPatient(patientId);
        history.setPatient(patient);
        MedicalHistory saved = medicalHistoryRepository.save(history);
        log.info("added medical history [{}] for patient [{}]", saved.getId(), patientId);
        return saved;
    }

    @Override
    public List<MedicalHistory> getMedicalHistories(Long patientId) {
        return medicalHistoryRepository.findByPatientId(patientId);
    }

    // Add prescription
    @Override
    public PrescriptionHistory addPrescription(Long patientId, PrescriptionHistory prescription) {
        Patient patient = getPatient(patientId);
        prescription.setPatient(patient);
        PrescriptionHistory saved = prescriptionRepository.save(prescription);
        log.info("added prescription [{}] for patient [{}]", saved.getId(), patientId);
        return saved;
    }

    @Override
    public List<PrescriptionHistory> getPrescriptions(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    // Loyalty program
    @Override
    public LoyaltyProgram createOrUpdateLoyalty(Long patientId, int points, String status) {
        Patient patient = getPatient(patientId);
        LoyaltyProgram loyalty = loyaltyRepository.findByPatientId(patientId)
                .orElse(new LoyaltyProgram());
        loyalty.setPatient(patient);
        loyalty.setPoints(points);
        loyalty.setStatus(status);
        LoyaltyProgram saved = loyaltyRepository.save(loyalty);
        log.info("updated loyalty [{}] for patient [{}]", saved.getId(), patientId);
        return saved;
    }

    @Override
    public LoyaltyProgram getLoyalty(Long patientId) {
        return loyaltyRepository.findByPatientId(patientId)
                .orElseThrow(() -> new RuntimeException("Loyalty program not found"));
    }

    public Patient getPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        log.info("viewed patient [{}]", patientId);
        return patient;
    }

}
