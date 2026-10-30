package com.healthconnect.patient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthconnect.patient.entity.Patient;
import com.healthconnect.patient.service.PatientService;
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

class PatientControllerTest {

    private PatientService service;
    private MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        service = mock(PatientService.class);
        mvc = MockMvcBuilders.standaloneSetup(new PatientController(service)).build();
    }

    @Test
    void create_returnsPatient() throws Exception {
        Patient patient = patient(1L, "Asha");
        when(service.create(any(Patient.class))).thenReturn(patient);

        mvc.perform(post("/api/patients").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.name").value("Asha"));
    }

    @Test
    void getAll_returnsPatients() throws Exception {
        when(service.getAll()).thenReturn(List.of(patient(1L, "Asha")));

        mvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Asha"));
    }

    @Test
    void getById_returnsPatient() throws Exception {
        when(service.getById(1L)).thenReturn(patient(1L, "Asha"));

        mvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value(1));
    }

    @Test
    void update_returnsUpdatedPatient() throws Exception {
        when(service.update(eq(1L), any(Patient.class))).thenReturn(patient(1L, "Updated"));

        mvc.perform(put("/api/patients/1").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void delete_returnsOk() throws Exception {
        mvc.perform(delete("/api/patients/1"))
                .andExpect(status().isOk());
        verify(service).delete(1L);
    }

    @Test
    void invalidId_returnsBadRequest() throws Exception {
        mvc.perform(get("/api/patients/not-a-number"))
                .andExpect(status().isBadRequest());
    }

    private Patient patient(Long id, String name) {
        Patient patient = new Patient();
        patient.setPatientId(id);
        patient.setName(name);
        return patient;
    }
}
