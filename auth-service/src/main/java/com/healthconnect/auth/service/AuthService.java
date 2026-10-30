package com.healthconnect.auth.service;

import org.springframework.stereotype.Service;
import com.healthconnect.auth.entity.User;
import com.healthconnect.auth.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository repo;

    public AuthService(UserRepository repo) {
        this.repo = repo;
    }

    public User register(User user) {
        return repo.save(user);
    }

    public String login(String username, String password) {
        User user = repo.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return com.healthconnect.auth.security.JwtUtil.generateToken(username);
        }

        return "INVALID_CREDENTIALS";
    }
}