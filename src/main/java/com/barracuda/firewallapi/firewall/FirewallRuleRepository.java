package com.barracuda.firewallapi.firewall;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FirewallRuleRepository extends JpaRepository<FirewallRule, Long> {

    // Find all rules for a given action (ALLOW or DENY)
    List<FirewallRule> findByAction(RuleAction action);

    // Find all enabled or disabled rules
    List<FirewallRule> findByEnabled(boolean enabled);

    // Find rules by protocol (TCP, UDP etc.)
    List<FirewallRule> findByProtocol(String protocol);

    // Find rules by source IP
    List<FirewallRule> findBySourceIp(String sourceIp);
}
