package com.web.pharma.customer.repos;

import com.web.pharma.customer.entities.LoyaltyProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoyaltyProgramRepository extends JpaRepository<LoyaltyProgram, Long> {
    Optional<LoyaltyProgram> findByPatientId(Long patientId);
}

