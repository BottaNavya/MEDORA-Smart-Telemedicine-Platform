package com.healthconnect.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.healthconnect.auth.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}