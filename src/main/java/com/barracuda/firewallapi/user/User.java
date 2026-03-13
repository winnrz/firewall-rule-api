package com.barracuda.firewallapi.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// @Entity tells JPA this class maps to a database table
// @Data (Lombok) auto-generates getters, setters, equals, hashCode, toString
// @Builder allows: User.builder().username("admin").build()
// @NoArgsConstructor / @AllArgsConstructor generate constructors
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // stored as bcrypt hash, never plain text

    @Enumerated(EnumType.STRING)
    private Role role;

    // UserDetails methods — Spring Security uses these to manage auth

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Prefix with ROLE_ is required by Spring Security
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
