package com.barracuda.firewallapi.firewall;

import com.barracuda.firewallapi.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// The service layer contains business logic
// Controllers call services, services call repositories
// This separation makes code easier to test and maintain
@Service
@RequiredArgsConstructor
public class FirewallRuleService {

    private final FirewallRuleRepository repository;

    public List<FirewallRule> getAllRules() {
        return repository.findAll();
    }

    public FirewallRule getRuleById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Firewall rule not found with id: " + id));
    }

    public List<FirewallRule> getRulesByAction(RuleAction action) {
        return repository.findByAction(action);
    }

    public List<FirewallRule> getEnabledRules() {
        return repository.findByEnabled(true);
    }

    public FirewallRule createRule(FirewallRuleRequest request) {
        FirewallRule rule = FirewallRule.builder()
                .name(request.getName())
                .sourceIp(request.getSourceIp())
                .destinationIp(request.getDestinationIp())
                .port(request.getPort())
                .protocol(request.getProtocol().toUpperCase())
                .action(request.getAction())
                .description(request.getDescription())
                .enabled(request.isEnabled())
                .build();

        return repository.save(rule);
    }

    public FirewallRule updateRule(Long id, FirewallRuleRequest request) {
        // Fetch existing rule — throws 404 if not found
        FirewallRule existing = getRuleById(id);

        // Update fields
        existing.setName(request.getName());
        existing.setSourceIp(request.getSourceIp());
        existing.setDestinationIp(request.getDestinationIp());
        existing.setPort(request.getPort());
        existing.setProtocol(request.getProtocol().toUpperCase());
        existing.setAction(request.getAction());
        existing.setDescription(request.getDescription());
        existing.setEnabled(request.isEnabled());

        return repository.save(existing);
    }

    public void deleteRule(Long id) {
        FirewallRule rule = getRuleById(id); // Validates existence first
        repository.delete(rule);
    }

    public FirewallRule toggleRule(Long id) {
        FirewallRule rule = getRuleById(id);
        rule.setEnabled(!rule.isEnabled());
        return repository.save(rule);
    }
}
