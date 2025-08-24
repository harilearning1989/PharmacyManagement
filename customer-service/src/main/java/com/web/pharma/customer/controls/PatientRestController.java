package com.web.pharma.customer.controls;

import com.web.pharma.customer.entities.LoyaltyProgram;
import com.web.pharma.customer.entities.MedicalHistory;
import com.web.pharma.customer.entities.Patient;
import com.web.pharma.customer.entities.PrescriptionHistory;
import com.web.pharma.customer.services.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientRestController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.savePatient(patient));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatient(id));
    }

    @PostMapping("/{id}/medical-history")
    public ResponseEntity<MedicalHistory> addMedicalHistory(@PathVariable Long id, @RequestBody MedicalHistory history) {
        return ResponseEntity.ok(patientService.addMedicalHistory(id, history));
    }

    @GetMapping("/{id}/medical-history")
    public ResponseEntity<List<MedicalHistory>> getMedicalHistory(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getMedicalHistories(id));
    }

    @PostMapping("/{id}/prescriptions")
    public ResponseEntity<PrescriptionHistory> addPrescription(@PathVariable Long id, @RequestBody PrescriptionHistory prescription) {
        return ResponseEntity.ok(patientService.addPrescription(id, prescription));
    }

    @GetMapping("/{id}/prescriptions")
    public ResponseEntity<List<PrescriptionHistory>> getPrescriptions(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPrescriptions(id));
    }

    @PostMapping("/{id}/loyalty")
    public ResponseEntity<LoyaltyProgram> updateLoyalty(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        int points = (Integer) request.get("points");
        String status = (String) request.get("status");
        return ResponseEntity.ok(patientService.createOrUpdateLoyalty(id, points, status));
    }

    @GetMapping("/{id}/loyalty")
    public ResponseEntity<LoyaltyProgram> getLoyalty(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getLoyalty(id));
    }
}

