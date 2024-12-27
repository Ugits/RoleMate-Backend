package org.jonas.rolemate_backend.character.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CharacterLevelDTO(
        @NotNull(message = "Level must not be null")
        @Min(value = 0, message = "Level must be a non-negative integer")
        int level
) {
}
