package com.healthconnect.patient.service;

import com.healthconnect.patient.entity.Patient;
import com.healthconnect.patient.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository repository;

    @InjectMocks
    private PatientService service;

    @Test
    void create_savesAndReturnsPatient() {
        Patient patient = patient("Asha");
        when(repository.save(patient)).thenReturn(patient);

        assertSame(patient, service.create(patient));
        verify(repository).save(patient);
    }

    @Test
    void getAll_returnsRepositoryResults() {
        List<Patient> patients = List.of(patient("Asha"));
        when(repository.findAll()).thenReturn(patients);

        assertSame(patients, service.getAll());
    }

    @Test
    void getById_whenMissing_returnsNull() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertNull(service.getById(99L));
    }

    @Test
    void update_whenPresent_copiesEditableFieldsAndSaves() {
        Patient existing = patient("Old");
        Patient update = patient("New");
        update.setEmail("new@example.com");
        update.setPhone("555");
        update.setAge(30);
        update.setGender("F");
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        assertSame(existing, service.update(1L, update));
        assertEquals("New", existing.getName());
        assertEquals("new@example.com", existing.getEmail());
        assertEquals(30, existing.getAge());
        verify(repository).save(existing);
    }

    @Test
    void update_whenMissing_returnsNullAndDoesNotSave() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertNull(service.update(1L, patient("New")));
        verify(repository, never()).save(any());
    }

    @Test
    void delete_delegatesToRepository() {
        service.delete(1L);
        verify(repository).deleteById(1L);
    }

    private Patient patient(String name) {
        Patient patient = new Patient();
        patient.setName(name);
        return patient;
    }
}
