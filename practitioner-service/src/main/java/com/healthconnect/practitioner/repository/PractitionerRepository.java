package com.healthconnect.practitioner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.healthconnect.practitioner.entity.Practitioner;

public interface PractitionerRepository extends JpaRepository<Practitioner, Long> {
}