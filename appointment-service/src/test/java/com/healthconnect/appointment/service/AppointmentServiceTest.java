package com.healthconnect.appointment.service;

import com.healthconnect.appointment.entity.Appointment;
import com.healthconnect.appointment.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository repository;

    @InjectMocks
    private AppointmentService service;

    @Test
    void create_setsRequestedStatusAndCreatedAt() {
        Appointment appointment = new Appointment();
        when(repository.save(appointment)).thenReturn(appointment);

        Appointment result = service.create(appointment);

        assertSame(appointment, result);
        assertEquals("REQUESTED", appointment.getStatus());
        assertNotNull(appointment.getCreatedAt());
        verify(repository).save(appointment);
    }

    @Test
    void getAll_returnsRepositoryResults() {
        List<Appointment> appointments = List.of(new Appointment());
        when(repository.findAll()).thenReturn(appointments);

        assertSame(appointments, service.getAll());
    }

    @Test
    void getById_whenMissing_throwsNotFoundException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.getById(1L));

        assertEquals("Appointment not found", exception.getMessage());
    }

    @Test
    void update_copiesMutableFieldsAndSaves() {
        Appointment existing = new Appointment();
        Appointment updated = new Appointment();
        LocalDateTime time = LocalDateTime.now().plusDays(1);
        updated.setAppointmentTime(time);
        updated.setReason("Follow-up");
        updated.setStatus("CONFIRMED");
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        assertSame(existing, service.update(1L, updated));
        assertEquals(time, existing.getAppointmentTime());
        assertEquals("Follow-up", existing.getReason());
        assertEquals("CONFIRMED", existing.getStatus());
    }

    @Test
    void delete_delegatesToRepository() {
        service.delete(1L);
        verify(repository).deleteById(1L);
    }
}
