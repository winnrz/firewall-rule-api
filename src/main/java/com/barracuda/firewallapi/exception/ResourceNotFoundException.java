package com.barracuda.firewallapi.exception;

// Custom exception for when a requested resource doesn't exist
// Extends RuntimeException so we don't need to declare it in method signatures
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
