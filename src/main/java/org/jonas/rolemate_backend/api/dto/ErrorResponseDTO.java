package org.jonas.rolemate_backend.api.dto;

public record ErrorResponseDTO(
        int statusCode,
        String message,
        String timestamp
) {
}
