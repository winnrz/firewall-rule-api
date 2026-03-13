package com.barracuda.firewallapi.auth;

import com.barracuda.firewallapi.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @RestController = @Controller + @ResponseBody
// Means every method returns JSON automatically
// @RequestMapping sets the base URL path for all methods in this class
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // POST /api/auth/register/admin — register an admin user
    @PostMapping("/register/admin")
    public ResponseEntity<AuthResponse> registerAdmin(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.register(request, Role.ADMIN));
    }

    // POST /api/auth/register/viewer — register a viewer user
    @PostMapping("/register/viewer")
    public ResponseEntity<AuthResponse> registerViewer(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.register(request, Role.VIEWER));
    }

    // POST /api/auth/login — returns a JWT token
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
