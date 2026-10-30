package com.healthconnect.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.healthconnect.patient.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}