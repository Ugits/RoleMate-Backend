package org.jonas.rolemate_backend.user.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCredentialsDTO(
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 5, max = 20, message = "Username size bust be between 5 and 20 characters")
        String username,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 7, max = 30, message = "Password size bust be between 7 and 30 characters")
        String password
) {
}
