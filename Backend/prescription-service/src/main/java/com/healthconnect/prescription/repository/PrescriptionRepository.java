package com.healthconnect.prescription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.healthconnect.prescription.entity.Prescription;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
}