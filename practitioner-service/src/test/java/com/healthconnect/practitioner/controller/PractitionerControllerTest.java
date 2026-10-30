package com.healthconnect.practitioner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthconnect.practitioner.entity.Practitioner;
import com.healthconnect.practitioner.service.PractitionerService;
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

class PractitionerControllerTest {

    private PractitionerService service;
    private MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        service = mock(PractitionerService.class);
        mvc = MockMvcBuilders.standaloneSetup(new PractitionerController(service)).build();
    }

    @Test
    void create_getAll_getById_areSuccessful() throws Exception {
        Practitioner practitioner = practitioner(1L, "Dr Rao");
        when(service.create(any())).thenReturn(practitioner);
        when(service.getAll()).thenReturn(List.of(practitioner));
        when(service.getById(1L)).thenReturn(practitioner);

        mvc.perform(post("/api/practitioners").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(practitioner)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Dr Rao"));
        mvc.perform(get("/api/practitioners")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].practitionerId").value(1));
        mvc.perform(get("/api/practitioners/1")).andExpect(status().isOk());
    }

    @Test
    void update_returnsUpdatedPractitioner() throws Exception {
        when(service.update(eq(1L), any())).thenReturn(practitioner(1L, "Updated"));

        mvc.perform(put("/api/practitioners/1").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void delete_returnsConfirmation() throws Exception {
        mvc.perform(delete("/api/practitioners/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Practitioner deleted successfully"));
        verify(service).delete(1L);
    }

    @Test
    void malformedBody_returnsBadRequest() throws Exception {
        mvc.perform(post("/api/practitioners").contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest());
    }

    private Practitioner practitioner(Long id, String name) {
        Practitioner practitioner = new Practitioner();
        practitioner.setPractitionerId(id);
        practitioner.setName(name);
        return practitioner;
    }
}
