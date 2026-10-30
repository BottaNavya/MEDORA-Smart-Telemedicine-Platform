package com.healthconnect.appointment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.healthconnect.appointment.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}