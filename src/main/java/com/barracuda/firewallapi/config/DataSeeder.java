package com.barracuda.firewallapi.config;

import com.barracuda.firewallapi.firewall.FirewallRule;
import com.barracuda.firewallapi.firewall.FirewallRuleRepository;
import com.barracuda.firewallapi.firewall.RuleAction;
import com.barracuda.firewallapi.user.Role;
import com.barracuda.firewallapi.user.User;
import com.barracuda.firewallapi.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// CommandLineRunner runs this code once when the app starts
// Great for seeding demo data
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FirewallRuleRepository firewallRuleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedUsers();
        seedFirewallRules();
    }

    private void seedUsers() {
        if (userRepository.count() > 0) return;

        userRepository.save(User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .build());

        userRepository.save(User.builder()
                .username("viewer")
                .password(passwordEncoder.encode("viewer123"))
                .role(Role.VIEWER)
                .build());

        System.out.println("✓ Seeded users: admin / viewer");
    }

    private void seedFirewallRules() {
        if (firewallRuleRepository.count() > 0) return;

        firewallRuleRepository.save(FirewallRule.builder()
                .name("Block External SSH")
                .sourceIp("0.0.0.0/0")
                .destinationIp("10.0.0.0/8")
                .port(22)
                .protocol("TCP")
                .action(RuleAction.DENY)
                .description("Block all external SSH access to internal network")
                .enabled(true)
                .build());

        firewallRuleRepository.save(FirewallRule.builder()
                .name("Allow HTTPS Outbound")
                .sourceIp("10.0.0.0/8")
                .destinationIp("0.0.0.0/0")
                .port(443)
                .protocol("TCP")
                .action(RuleAction.ALLOW)
                .description("Allow all internal hosts to reach HTTPS services")
                .enabled(true)
                .build());

        firewallRuleRepository.save(FirewallRule.builder()
                .name("Allow DNS")
                .sourceIp("10.0.0.0/8")
                .destinationIp("8.8.8.8")
                .port(53)
                .protocol("UDP")
                .action(RuleAction.ALLOW)
                .description("Allow DNS resolution to Google DNS")
                .enabled(true)
                .build());

        firewallRuleRepository.save(FirewallRule.builder()
                .name("Block Telnet")
                .sourceIp("0.0.0.0/0")
                .destinationIp("0.0.0.0/0")
                .port(23)
                .protocol("TCP")
                .action(RuleAction.DENY)
                .description("Block insecure Telnet protocol")
                .enabled(true)
                .build());

        System.out.println("✓ Seeded firewall rules");
    }
}
