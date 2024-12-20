package org.jonas.rolemate_backend.user.model.dto;

public record updateAccountStatusDTO(
        String username,
        boolean isEnabled
) {
}
