package com.healthconnect.appointment.integration;

import com.healthconnect.appointment.repository.AppointmentRepository;
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
        "spring.datasource.url=jdbc:h2:mem:appointment-it;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "eureka.client.enabled=false"
})
class AppointmentServiceIntegrationTest {

    @Autowired private MockMvc mvc;
    @Autowired private AppointmentRepository repository;

    @Test
    void createReadUpdateDeleteWorkflow_setsRequestedStatus() throws Exception {
        mvc.perform(post("/api/appointments").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"patientId\":\"p1\",\"practitionerId\":\"d1\",\"reason\":\"Consultation\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("REQUESTED"));
        Long id = repository.findAll().get(0).getAppointmentId();

        mvc.perform(get("/api/appointments/" + id)).andExpect(status().isOk());
        mvc.perform(put("/api/appointments/" + id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reason\":\"Follow-up\",\"status\":\"CONFIRMED\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("CONFIRMED"));
        mvc.perform(delete("/api/appointments/" + id)).andExpect(status().isOk());
        assertFalse(repository.existsById(id));
    }
}
