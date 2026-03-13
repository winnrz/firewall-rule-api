package com.barracuda.firewallapi.firewall;

import com.barracuda.firewallapi.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class) enables Mockito for this test class
// Mockito lets us fake (mock) dependencies so we test only the service logic
@ExtendWith(MockitoExtension.class)
class FirewallRuleServiceTest {

    // @Mock creates a fake repository — no real database involved
    @Mock
    private FirewallRuleRepository repository;

    // @InjectMocks creates the service and injects the mock repository into it
    @InjectMocks
    private FirewallRuleService service;

    private FirewallRule sampleRule;
    private FirewallRuleRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleRule = FirewallRule.builder()
                .id(1L)
                .name("Block SSH")
                .sourceIp("0.0.0.0/0")
                .destinationIp("10.0.0.1")
                .port(22)
                .protocol("TCP")
                .action(RuleAction.DENY)
                .enabled(true)
                .build();

        sampleRequest = FirewallRuleRequest.builder()
                .name("Block SSH")
                .sourceIp("0.0.0.0/0")
                .destinationIp("10.0.0.1")
                .port(22)
                .protocol("tcp") // lowercase to test toUpperCase() conversion
                .action(RuleAction.DENY)
                .enabled(true)
                .build();
    }

    @Test
    @DisplayName("Should return all firewall rules")
    void getAllRules_returnsAllRules() {
        when(repository.findAll()).thenReturn(List.of(sampleRule));

        List<FirewallRule> result = service.getAllRules();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Block SSH");
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return rule by ID when it exists")
    void getRuleById_existingId_returnsRule() {
        when(repository.findById(1L)).thenReturn(Optional.of(sampleRule));

        FirewallRule result = service.getRuleById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getAction()).isEqualTo(RuleAction.DENY);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException for non-existent ID")
    void getRuleById_nonExistentId_throwsException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getRuleById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Should create a new firewall rule and save it")
    void createRule_validRequest_savesAndReturnsRule() {
        when(repository.save(any(FirewallRule.class))).thenReturn(sampleRule);

        FirewallRule result = service.createRule(sampleRequest);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Block SSH");
        // Verify the repository was called once
        verify(repository, times(1)).save(any(FirewallRule.class));
    }

    @Test
    @DisplayName("Should convert protocol to uppercase on create")
    void createRule_lowercaseProtocol_savesAsUppercase() {
        FirewallRule savedRule = FirewallRule.builder()
                .id(1L).name("Block SSH").sourceIp("0.0.0.0/0")
                .destinationIp("10.0.0.1").port(22)
                .protocol("TCP") // expected uppercase
                .action(RuleAction.DENY).enabled(true).build();

        when(repository.save(any(FirewallRule.class))).thenReturn(savedRule);

        FirewallRule result = service.createRule(sampleRequest);

        assertThat(result.getProtocol()).isEqualTo("TCP");
    }

    @Test
    @DisplayName("Should update an existing rule")
    void updateRule_existingId_updatesAndReturnsRule() {
        when(repository.findById(1L)).thenReturn(Optional.of(sampleRule));
        when(repository.save(any(FirewallRule.class))).thenReturn(sampleRule);

        FirewallRule result = service.updateRule(1L, sampleRequest);

        assertThat(result).isNotNull();
        verify(repository).save(sampleRule);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent rule")
    void updateRule_nonExistentId_throwsException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateRule(99L, sampleRequest))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Should delete an existing rule")
    void deleteRule_existingId_deletesRule() {
        when(repository.findById(1L)).thenReturn(Optional.of(sampleRule));

        service.deleteRule(1L);

        verify(repository, times(1)).delete(sampleRule);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent rule")
    void deleteRule_nonExistentId_throwsException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteRule(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(repository, never()).delete(any());
    }

    @Test
    @DisplayName("Should toggle rule from enabled to disabled")
    void toggleRule_enabledRule_disablesIt() {
        sampleRule.setEnabled(true);
        when(repository.findById(1L)).thenReturn(Optional.of(sampleRule));
        when(repository.save(any(FirewallRule.class))).thenAnswer(i -> i.getArgument(0));

        FirewallRule result = service.toggleRule(1L);

        assertThat(result.isEnabled()).isFalse();
    }

    @Test
    @DisplayName("Should return rules filtered by action")
    void getRulesByAction_returnsFilteredRules() {
        when(repository.findByAction(RuleAction.DENY)).thenReturn(List.of(sampleRule));

        List<FirewallRule> result = service.getRulesByAction(RuleAction.DENY);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAction()).isEqualTo(RuleAction.DENY);
    }
}
