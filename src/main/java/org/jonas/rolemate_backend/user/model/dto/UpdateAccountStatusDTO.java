package org.jonas.rolemate_backend.user.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateAccountStatusDTO(
        String username,
        boolean isEnabled
) {
}
