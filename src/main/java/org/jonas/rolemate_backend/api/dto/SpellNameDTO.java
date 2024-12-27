package org.jonas.rolemate_backend.api.dto;

import jakarta.validation.constraints.NotBlank;

public record SpellNameDTO(
        @NotBlank
        String name
) {
}
