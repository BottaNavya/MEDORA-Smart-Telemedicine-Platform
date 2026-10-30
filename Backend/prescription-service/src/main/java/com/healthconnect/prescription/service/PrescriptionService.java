package com.healthconnect.prescription.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.healthconnect.prescription.entity.Prescription;
import com.healthconnect.prescription.repository.PrescriptionRepository;

@Service
public class PrescriptionService {

    private final PrescriptionRepository repository;

    public PrescriptionService(PrescriptionRepository repository) {
        this.repository = repository;
    }

    public Prescription create(Prescription p) {
        p.setIssuedDate(LocalDateTime.now());
        return repository.save(p);
    }

    public List<Prescription> getAll() {
        return repository.findAll();
    }

    public Prescription getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}