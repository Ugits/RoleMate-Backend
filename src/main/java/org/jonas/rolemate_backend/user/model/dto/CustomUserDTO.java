package org.jonas.rolemate_backend.user.model.dto;

import org.jonas.rolemate_backend.character.model.dto.CharacterDTO;

import java.util.List;

public record CustomUserDTO(
        Long id,
        String username,
        List<CharacterDTO> characters
) {
}
