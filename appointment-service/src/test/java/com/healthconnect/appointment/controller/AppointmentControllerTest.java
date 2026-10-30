package com.healthconnect.appointment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthconnect.appointment.entity.Appointment;
import com.healthconnect.appointment.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AppointmentControllerTest {

    private AppointmentService service;
    private MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        service = mock(AppointmentService.class);
        mvc = MockMvcBuilders.standaloneSetup(new AppointmentController(service)).build();
    }

    @Test
    void create_getAll_getById_areSuccessful() throws Exception {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1L);
        when(service.create(any())).thenReturn(appointment);
        when(service.getAll()).thenReturn(List.of(appointment));
        when(service.getById(1L)).thenReturn(appointment);

        mvc.perform(post("/api/appointments").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(appointment)))
                .andExpect(status().isOk());
        mvc.perform(get("/api/appointments")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].appointmentId").value(1));
        mvc.perform(get("/api/appointments/1")).andExpect(status().isOk());
    }

    @Test
    void update_andDelete_areSuccessful() throws Exception {
        when(service.update(eq(1L), any())).thenReturn(new Appointment());

        mvc.perform(put("/api/appointments/1").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reason\":\"Follow-up\"}"))
                .andExpect(status().isOk());
        mvc.perform(delete("/api/appointments/1")).andExpect(status().isOk());
        verify(service).delete(1L);
    }

    @Test
    void invalidId_returnsBadRequest() throws Exception {
        mvc.perform(get("/api/appointments/nope")).andExpect(status().isBadRequest());
    }
}
