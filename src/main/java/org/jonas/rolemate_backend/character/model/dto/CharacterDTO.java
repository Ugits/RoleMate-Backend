package org.jonas.rolemate_backend.character.model.dto;

import org.jonas.rolemate_backend.character.model.entity.CharacterEntity;

public record CharacterDTO(
        Long id,
        String name,
        int level,
        int strength,
        int dexterity,
        int constitution,
        int intelligence,
        int wisdom,
        int charisma
) {

    public CharacterDTO(CharacterEntity character) {
        this(
                character.getId(),
                character.getName(),
                character.getLevel(),
                character.getStrength(),
                character.getDexterity(),
                character.getConstitution(),
                character.getIntelligence(),
                character.getWisdom(),
                character.getCharisma()
        );
    }
}
