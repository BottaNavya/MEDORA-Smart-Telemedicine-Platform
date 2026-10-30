package com.healthconnect.prescription.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthconnect.prescription.entity.Prescription;
import com.healthconnect.prescription.service.PrescriptionService;
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

class PrescriptionControllerTest {

    private PrescriptionService service;
    private MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        service = mock(PrescriptionService.class);
        mvc = MockMvcBuilders.standaloneSetup(new PrescriptionController(service)).build();
    }

    @Test
    void create_getAll_getById_areSuccessful() throws Exception {
        Prescription prescription = new Prescription();
        prescription.setPrescriptionId(1L);
        when(service.create(any())).thenReturn(prescription);
        when(service.getAll()).thenReturn(List.of(prescription));
        when(service.getById(1L)).thenReturn(prescription);

        mvc.perform(post("/api/prescriptions").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(prescription)))
                .andExpect(status().isOk());
        mvc.perform(get("/api/prescriptions")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].prescriptionId").value(1));
        mvc.perform(get("/api/prescriptions/1")).andExpect(status().isOk());
    }

    @Test
    void delete_returnsOk() throws Exception {
        mvc.perform(delete("/api/prescriptions/1")).andExpect(status().isOk());
        verify(service).delete(1L);
    }

    @Test
    void malformedBody_returnsBadRequest() throws Exception {
        mvc.perform(post("/api/prescriptions").contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest());
    }
}
