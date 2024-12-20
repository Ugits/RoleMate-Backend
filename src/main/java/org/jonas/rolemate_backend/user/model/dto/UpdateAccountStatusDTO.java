package org.jonas.rolemate_backend.user.model.dto;

public record UpdateAccountStatusDTO(
        String username,
        boolean isEnabled
) {
}
