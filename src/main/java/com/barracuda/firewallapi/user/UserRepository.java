package com.barracuda.firewallapi.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// JpaRepository gives us free CRUD methods:
// save(), findById(), findAll(), deleteById() etc.
// We just add custom queries we need on top
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA auto-generates the SQL from the method name:
    // SELECT * FROM users WHERE username = ?
    Optional<User> findByUsername(String username);
}
