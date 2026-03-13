package com.barracuda.firewallapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication combines:
// - @Configuration (this class defines Spring beans)
// - @EnableAutoConfiguration (Spring auto-configures based on dependencies)
// - @ComponentScan (Spring scans this package for components)
@SpringBootApplication
public class FirewallApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FirewallApiApplication.class, args);
    }
}
