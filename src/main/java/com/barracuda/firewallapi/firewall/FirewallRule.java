package com.barracuda.firewallapi.firewall;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "firewall_rules")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FirewallRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Rule name is required")
    private String name;

    @NotBlank(message = "Source IP is required")
    private String sourceIp;

    @NotBlank(message = "Destination IP is required")
    private String destinationIp;

    private Integer port; // null means all ports

    @NotBlank(message = "Protocol is required")
    private String protocol; // TCP, UDP, ICMP, ANY

    @NotNull(message = "Action is required")
    @Enumerated(EnumType.STRING)
    private RuleAction action; // ALLOW or DENY

    private String description;

    private boolean enabled;

    // Automatically set on creation
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        enabled = true; // rules are enabled by default
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
