package com.healthconnect.auth.security;

import com.healthconnect.auth.entity.User;
import com.healthconnect.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:security-it;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "eureka.client.enabled=false"
})
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository repository;

    @Test
    void registrationAndLogin_arePublic() throws Exception {
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"security-user\",\"password\":\"secret\"}"))
                .andExpect(status().isOk());

        mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"security-user\",\"password\":\"secret\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.not("INVALID_CREDENTIALS")));
    }

    @Test
    void incorrectPassword_isRejectedByApplication() throws Exception {
        User user = new User();
        user.setUsername("security-wrong");
        user.setPassword("correct");
        repository.save(user);

        mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"security-wrong\",\"password\":\"wrong\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("INVALID_CREDENTIALS"));
    }

    @Test
    void missingAndInvalidBearerTokens_areUnauthorizedForProtectedEndpoint() throws Exception {
        mvc.perform(get("/protected-resource")).andExpect(status().isForbidden());
        mvc.perform(get("/protected-resource").header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isForbidden());
    }
}
