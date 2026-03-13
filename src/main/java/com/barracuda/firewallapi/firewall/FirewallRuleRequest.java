package com.barracuda.firewallapi.firewall;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO used for create and update requests
// We use a DTO instead of the entity directly to control what the client can send
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FirewallRuleRequest {

    @NotBlank(message = "Rule name is required")
    private String name;

    @NotBlank(message = "Source IP is required")
    private String sourceIp;

    @NotBlank(message = "Destination IP is required")
    private String destinationIp;

    private Integer port;

    @NotBlank(message = "Protocol is required")
    private String protocol;

    @NotNull(message = "Action is required")
    private RuleAction action;

    private String description;

    private boolean enabled;
}
