package com.healthconnect.practitioner.service;

import com.healthconnect.practitioner.entity.Practitioner;
import com.healthconnect.practitioner.repository.PractitionerRepository;
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
class PractitionerServiceTest {

    @Mock
    private PractitionerRepository repository;

    @InjectMocks
    private PractitionerService service;

    @Test
    void create_savesPractitioner() {
        Practitioner practitioner = practitioner("Dr Rao");
        when(repository.save(practitioner)).thenReturn(practitioner);

        assertSame(practitioner, service.create(practitioner));
        verify(repository).save(practitioner);
    }

    @Test
    void getAll_returnsAllPractitioners() {
        List<Practitioner> practitioners = List.of(practitioner("Dr Rao"));
        when(repository.findAll()).thenReturn(practitioners);

        assertEquals(practitioners, service.getAll());
    }

    @Test
    void getById_whenMissing_returnsNull() {
        when(repository.findById(9L)).thenReturn(Optional.empty());

        assertNull(service.getById(9L));
    }

    @Test
    void update_whenPresent_copiesFields() {
        Practitioner existing = practitioner("Old");
        Practitioner update = practitioner("New");
        update.setSpecialization("Cardiology");
        update.setConsultationFee(800);
        update.setAvailabilityStatus("AVAILABLE");
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        assertSame(existing, service.update(1L, update));
        assertEquals("New", existing.getName());
        assertEquals("Cardiology", existing.getSpecialization());
        assertEquals(800, existing.getConsultationFee());
        assertEquals("AVAILABLE", existing.getAvailabilityStatus());
    }

    @Test
    void update_whenMissing_throwsNotFoundException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> service.update(1L, practitioner("New")));

        assertEquals("Practitioner not found", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void delete_delegatesToRepository() {
        service.delete(1L);
        verify(repository).deleteById(1L);
    }

    private Practitioner practitioner(String name) {
        Practitioner practitioner = new Practitioner();
        practitioner.setName(name);
        return practitioner;
    }
}
