package org.jonas.rolemate_backend.auth.dto;

public record AuthRequest(
        String username,
        String password
) {
}
