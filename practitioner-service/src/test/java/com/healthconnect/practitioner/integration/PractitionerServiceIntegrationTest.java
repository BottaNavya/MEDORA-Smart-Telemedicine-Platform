package com.healthconnect.practitioner.integration;

import com.healthconnect.practitioner.repository.PractitionerRepository;
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
        "spring.datasource.url=jdbc:h2:mem:practitioner-it;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "eureka.client.enabled=false"
})
class PractitionerServiceIntegrationTest {

    @Autowired private MockMvc mvc;
    @Autowired private PractitionerRepository repository;

    @Test
    void crudWorkflow_persistsAndUpdatesPractitioner() throws Exception {
        mvc.perform(post("/api/practitioners").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Dr Rao\",\"specialization\":\"Cardiology\",\"consultationFee\":500}"))
                .andExpect(status().isOk());
        Long id = repository.findAll().get(0).getPractitionerId();

        mvc.perform(put("/api/practitioners/" + id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Dr Rao Updated\",\"specialization\":\"Neurology\",\"consultationFee\":700}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.specialization").value("Neurology"));
        mvc.perform(get("/api/practitioners/" + id)).andExpect(status().isOk());
        mvc.perform(delete("/api/practitioners/" + id)).andExpect(status().isOk());
        assertFalse(repository.existsById(id));
    }
}
