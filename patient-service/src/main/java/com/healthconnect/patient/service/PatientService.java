package com.healthconnect.patient.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.healthconnect.patient.entity.Patient;
import com.healthconnect.patient.repository.PatientRepository;

@Service
public class PatientService {

    private final PatientRepository repository;

    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    public Patient create(Patient p) {
        return repository.save(p);
    }

    public List<Patient> getAll() {
        return repository.findAll();
    }

    public Patient getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Patient update(Long id, Patient p) {
        Patient existing = repository.findById(id).orElse(null);
        if (existing != null) {
            existing.setName(p.getName());
            existing.setEmail(p.getEmail());
            existing.setPhone(p.getPhone());
            existing.setAge(p.getAge());
            existing.setGender(p.getGender());
            return repository.save(existing);
        }
        return null;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}