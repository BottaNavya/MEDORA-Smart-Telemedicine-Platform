package com.healthconnect.prescription.integration;

import com.healthconnect.prescription.repository.PrescriptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:prescription-it;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "eureka.client.enabled=false"
})
class PrescriptionServiceIntegrationTest {

    @Autowired private MockMvc mvc;
    @Autowired private PrescriptionRepository repository;

    @Test
    void createReadDeleteWorkflow_persistsPrescription() throws Exception {
        mvc.perform(post("/api/prescriptions").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"appointmentId\":7,\"medicationDetails\":\"Medicine\",\"instructions\":\"Once daily\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.appointmentId").value(7));
        Long id = repository.findAll().get(0).getPrescriptionId();

        mvc.perform(get("/api/prescriptions/" + id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.medicationDetails").value("Medicine"));
        mvc.perform(delete("/api/prescriptions/" + id)).andExpect(status().isOk());
        assertFalse(repository.existsById(id));
    }
}
