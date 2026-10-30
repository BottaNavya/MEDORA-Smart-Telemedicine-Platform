package com.healthconnect.prescription.service;

import com.healthconnect.prescription.entity.Prescription;
import com.healthconnect.prescription.repository.PrescriptionRepository;
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
class PrescriptionServiceTest {

    @Mock
    private PrescriptionRepository repository;

    @InjectMocks
    private PrescriptionService service;

    @Test
    void create_setsIssuedDateAndSaves() {
        Prescription prescription = new Prescription();
        when(repository.save(prescription)).thenReturn(prescription);

        assertSame(prescription, service.create(prescription));
        assertNotNull(prescription.getIssuedDate());
        verify(repository).save(prescription);
    }

    @Test
    void getAll_returnsRepositoryResults() {
        List<Prescription> prescriptions = List.of(new Prescription());
        when(repository.findAll()).thenReturn(prescriptions);

        assertSame(prescriptions, service.getAll());
    }

    @Test
    void getById_whenMissing_returnsNull() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertNull(service.getById(1L));
    }

    @Test
    void getById_whenPresent_returnsPrescription() {
        Prescription prescription = new Prescription();
        when(repository.findById(1L)).thenReturn(Optional.of(prescription));

        assertSame(prescription, service.getById(1L));
    }

    @Test
    void delete_delegatesToRepository() {
        service.delete(1L);
        verify(repository).deleteById(1L);
    }
}
