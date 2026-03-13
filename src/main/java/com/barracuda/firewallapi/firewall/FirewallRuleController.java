package com.barracuda.firewallapi.firewall;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/firewall-rules")
@RequiredArgsConstructor
public class FirewallRuleController {

    private final FirewallRuleService service;

    // GET /api/firewall-rules — get all rules (ADMIN + VIEWER)
    @GetMapping
    public ResponseEntity<List<FirewallRule>> getAllRules() {
        return ResponseEntity.ok(service.getAllRules());
    }

    // GET /api/firewall-rules/{id} — get a single rule (ADMIN + VIEWER)
    @GetMapping("/{id}")
    public ResponseEntity<FirewallRule> getRuleById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getRuleById(id));
    }

    // GET /api/firewall-rules/action/ALLOW — get rules by action (ADMIN + VIEWER)
    @GetMapping("/action/{action}")
    public ResponseEntity<List<FirewallRule>> getRulesByAction(@PathVariable RuleAction action) {
        return ResponseEntity.ok(service.getRulesByAction(action));
    }

    // GET /api/firewall-rules/enabled — get only active rules (ADMIN + VIEWER)
    @GetMapping("/enabled")
    public ResponseEntity<List<FirewallRule>> getEnabledRules() {
        return ResponseEntity.ok(service.getEnabledRules());
    }

    // POST /api/firewall-rules — create a new rule (ADMIN only)
    @PostMapping
    public ResponseEntity<FirewallRule> createRule(@Valid @RequestBody FirewallRuleRequest request) {
        // @Valid triggers validation annotations on the DTO
        // Returns 201 Created with the new rule
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createRule(request));
    }

    // PUT /api/firewall-rules/{id} — update a rule (ADMIN only)
    @PutMapping("/{id}")
    public ResponseEntity<FirewallRule> updateRule(
            @PathVariable Long id,
            @Valid @RequestBody FirewallRuleRequest request) {
        return ResponseEntity.ok(service.updateRule(id, request));
    }

    // DELETE /api/firewall-rules/{id} — delete a rule (ADMIN only)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long id) {
        service.deleteRule(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // PATCH /api/firewall-rules/{id}/toggle — enable/disable a rule (ADMIN only)
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<FirewallRule> toggleRule(@PathVariable Long id) {
        return ResponseEntity.ok(service.toggleRule(id));
    }
}
