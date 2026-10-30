package com.healthconnect.appointment.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.healthconnect.appointment.entity.Appointment;
import com.healthconnect.appointment.repository.AppointmentRepository;

@Service
public class AppointmentService {

    private final AppointmentRepository repository;

    public AppointmentService(AppointmentRepository repository) {
        this.repository = repository;
    }

    public Appointment create(Appointment a) {
        a.setStatus("REQUESTED"); // ✅ FIXED
        a.setCreatedAt(LocalDateTime.now()); // ✅ important
        return repository.save(a);
    }

    public List<Appointment> getAll() {
        return repository.findAll();
    }

    public Appointment getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    public Appointment update(Long id, Appointment updated) {
        Appointment a = getById(id);

        a.setAppointmentTime(updated.getAppointmentTime());
        a.setReason(updated.getReason());
        a.setStatus(updated.getStatus());

        return repository.save(a);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}