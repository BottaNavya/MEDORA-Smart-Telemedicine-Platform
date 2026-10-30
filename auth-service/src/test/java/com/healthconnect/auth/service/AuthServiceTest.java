package com.healthconnect.auth.service;

import com.healthconnect.auth.entity.User;
import com.healthconnect.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private AuthService service;

    @Test
    void register_returnsSavedUser() {
        User user = user("navya", "password");
        when(repository.save(user)).thenReturn(user);

        assertSame(user, service.register(user));
        verify(repository).save(user);
    }

    @Test
    void login_withMatchingCredentials_returnsJwt() {
        when(repository.findByUsername("navya")).thenReturn(user("navya", "password"));

        String token = service.login("navya", "password");

        assertNotNull(token);
        assertNotEquals("INVALID_CREDENTIALS", token);
        verify(repository).findByUsername("navya");
    }

    @Test
    void login_withUnknownUser_returnsInvalidCredentials() {
        when(repository.findByUsername("missing")).thenReturn(null);

        assertEquals("INVALID_CREDENTIALS", service.login("missing", "password"));
    }

    @Test
    void login_withWrongPassword_returnsInvalidCredentials() {
        when(repository.findByUsername("navya")).thenReturn(user("navya", "password"));

        assertEquals("INVALID_CREDENTIALS", service.login("navya", "wrong"));
    }

    private User user(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }
}
