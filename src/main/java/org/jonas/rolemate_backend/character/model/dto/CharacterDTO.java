package org.jonas.rolemate_backend.character.model.dto;

import org.jonas.rolemate_backend.character.model.entity.CharacterEntity;

public record CharacterDTO(
        Long id,
        String name,
        int level
) {
    public CharacterDTO(CharacterEntity character) {
        this(character.getId(), character.getName(), character.getLevel());
    }
}
