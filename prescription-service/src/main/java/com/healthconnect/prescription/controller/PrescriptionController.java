package com.healthconnect.prescription.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.healthconnect.prescription.entity.Prescription;
import com.healthconnect.prescription.service.PrescriptionService;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService service;

    public PrescriptionController(PrescriptionService service) {
        this.service = service;
    }

    @PostMapping
    public Prescription create(@RequestBody Prescription p) {
        return service.create(p);
    }

    @GetMapping
    public List<Prescription> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Prescription getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}