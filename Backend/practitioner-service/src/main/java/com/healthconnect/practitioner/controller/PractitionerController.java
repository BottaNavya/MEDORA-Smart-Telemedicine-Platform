package com.healthconnect.practitioner.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.healthconnect.practitioner.entity.Practitioner;
import com.healthconnect.practitioner.service.PractitionerService;

@RestController
@RequestMapping("/api/practitioners")
public class PractitionerController {

    private final PractitionerService service;

    // Constructor Injection
    public PractitionerController(PractitionerService service) {
        this.service = service;
    }

    // ✅ CREATE Practitioner
    @PostMapping
    public Practitioner create(@RequestBody Practitioner p) {
        return service.create(p);
    }

    // ✅ GET ALL Practitioners
    @GetMapping
    public List<Practitioner> getAll() {
        return service.getAll();
    }

    // ✅ GET Practitioner BY ID
    @GetMapping("/{id}")
    public Practitioner getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // ✅ UPDATE Practitioner
    @PutMapping("/{id}")
    public Practitioner update(@PathVariable Long id, @RequestBody Practitioner p) {
        return service.update(id, p);
    }

    // ✅ DELETE Practitioner
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Practitioner deleted successfully";
    }
}