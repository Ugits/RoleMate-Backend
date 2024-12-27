package org.jonas.rolemate_backend.character.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.jonas.rolemate_backend.character.model.entity.CharacterEntity;

public record CharacterDTO(
        Long id,
        @NotBlank(message = "Name is required.")
        String name,

        @NotNull
        @Min(value = 1, message = "Level must be at least 1.")
        @Max(value = 20, message = "Level must be at most 9.")
        Integer level,

        @NotNull
        @Min(value = 1, message = "Strength must be at least 1.")
        @Max(value = 30, message = "Strength must be at most 20.")
        Integer strength,

        @NotNull
        @Min(value = 1, message = "Dexterity must be at least 1.")
        @Max(value = 30, message = "Dexterity must be at most 20.")
        Integer dexterity,

        @NotNull
        @Min(value = 1, message = "Constitution must be at least 1.")
        @Max(value = 30, message = "Constitution must be at most 20.")
        Integer constitution,

        @NotNull
        @Min(value = 1, message = "Intelligence must be at least 1.")
        @Max(value = 30, message = "Intelligence must be at most 20.")
        Integer intelligence,

        @NotNull
        @Min(value = 1, message = "Wisdom must be at least 1.")
        @Max(value = 30, message = "Wisdom must be at most 20.")
        Integer wisdom,

        @NotNull
        @Min(value = 1, message = "Charisma must be at least 1.")
        @Max(value = 30, message = "Charisma must be at most 20.")
        Integer charisma
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
