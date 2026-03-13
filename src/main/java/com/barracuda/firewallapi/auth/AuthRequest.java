package com.barracuda.firewallapi.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO = Data Transfer Object
// These are simple classes used to carry data between the client and server
// They are NOT database entities — they don't map to tables

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    private String username;
    private String password;
}
