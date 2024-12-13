package org.jonas.rolemate_backend.auth.dto;

public record AuthResponse(
        String token,
        String role
) {
}
