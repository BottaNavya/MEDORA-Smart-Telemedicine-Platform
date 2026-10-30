package com.healthconnect.patient.integration;

import com.healthconnect.patient.entity.Patient;
import com.healthconnect.patient.repository.PatientRepository;
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
        "spring.datasource.url=jdbc:h2:mem:patient-it;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "eureka.client.enabled=false"
})
class PatientServiceIntegrationTest {

    @Autowired private MockMvc mvc;
    @Autowired private PatientRepository repository;

    @Test
    void crudWorkflow_persistsReadsUpdatesAndDeletes() throws Exception {
        String create = mvc.perform(post("/api/patients").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Asha\",\"email\":\"asha@example.com\",\"age\":28}"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Long id = repository.findAll().get(0).getPatientId();
        assertTrue(create.contains("Asha"));

        mvc.perform(get("/api/patients/" + id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("asha@example.com"));
        mvc.perform(put("/api/patients/" + id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated\",\"email\":\"updated@example.com\",\"age\":29}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Updated"));
        mvc.perform(delete("/api/patients/" + id)).andExpect(status().isOk());
        assertFalse(repository.existsById(id));
    }

    @Test
    void malformedJson_returnsBadRequest() throws Exception {
        mvc.perform(post("/api/patients").contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest());
    }
}
