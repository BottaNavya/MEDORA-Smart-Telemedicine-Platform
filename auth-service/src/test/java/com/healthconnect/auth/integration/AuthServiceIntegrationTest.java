package com.healthconnect.auth.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthconnect.auth.entity.User;
import com.healthconnect.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:auth-it;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "eureka.client.enabled=false"
})
class AuthServiceIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository repository;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void registerThenLogin_persistsUserAndReturnsJwt() throws Exception {
        String body = "{\"username\":\"integration-user\",\"password\":\"secret\",\"role\":\"PATIENT\"}";

        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());

        assertNotNull(repository.findByUsername("integration-user"));
        String response = mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertNotNull(response);
        assertFalse(response.isBlank());
        assertNotEquals("INVALID_CREDENTIALS", response);
    }

    @Test
    void loginWithWrongPassword_returnsInvalidCredentials() throws Exception {
        User user = new User();
        user.setUsername("wrong-password-user");
        user.setPassword("correct");
        repository.save(user);

        String response = mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"wrong-password-user\",\"password\":\"wrong\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals("INVALID_CREDENTIALS", response);
    }
}
