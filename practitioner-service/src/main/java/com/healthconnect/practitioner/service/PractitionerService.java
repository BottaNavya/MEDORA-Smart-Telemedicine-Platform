package com.healthconnect.practitioner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.healthconnect.practitioner.entity.Practitioner;
import com.healthconnect.practitioner.repository.PractitionerRepository;

@Service
public class PractitionerService {

    private final PractitionerRepository repository;

    // Constructor Injection
    public PractitionerService(PractitionerRepository repository) {
        this.repository = repository;
    }

    // ✅ CREATE
    public Practitioner create(Practitioner p) {
        return repository.save(p);
    }

    // ✅ GET ALL
    public List<Practitioner> getAll() {
        return repository.findAll();
    }

    // ✅ GET BY ID
    public Practitioner getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // ✅ UPDATE
    public Practitioner update(Long id, Practitioner p) {
        Practitioner existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Practitioner not found"));

        existing.setName(p.getName());
        existing.setSpecialization(p.getSpecialization());
        existing.setConsultationFee(p.getConsultationFee());
        existing.setAvailabilityStatus(p.getAvailabilityStatus());

        return repository.save(existing);
    }

    // ✅ DELETE
    public void delete(Long id) {
        repository.deleteById(id);
    }
}