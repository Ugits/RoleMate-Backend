package org.jonas.rolemate_backend.character.service;

import org.jonas.rolemate_backend.character.model.dto.CharacterDTO;
import org.jonas.rolemate_backend.character.model.entity.CharacterEntity;
import org.jonas.rolemate_backend.character.repository.CharacterRepository;
import org.jonas.rolemate_backend.exception.CharacterNotFoundException;
import org.jonas.rolemate_backend.exception.UnauthorizedException;
import org.jonas.rolemate_backend.user.model.entity.CustomUser;
import org.jonas.rolemate_backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final UserRepository userRepository;

    @Autowired
    public CharacterService(CharacterRepository characterRepository, UserRepository userRepository) {
        this.characterRepository = characterRepository;
        this.userRepository = userRepository;
    }

    public CharacterEntity createCharacter(String username, CharacterDTO charDTO) {

        CustomUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));

        CharacterEntity character = new CharacterEntity(
                charDTO.name(),
                charDTO.level(),
                user
        );
        return characterRepository.save(character);
    }

    public void deleteCharacter(String currentUsername, Long id) {

        CharacterEntity character = characterRepository.findById(id)
                .orElseThrow(() -> new CharacterNotFoundException("Character with ID: " + id + ", not found"));

        if (!character.getOwner().getUsername().equals(currentUsername)) {
            throw new UnauthorizedException("You are not authorized to delete this character.");
        }

        characterRepository.delete(character);
    }

    public List<CharacterEntity> getAllCharacters(String username) {

        CustomUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));

        return characterRepository.findByOwnerId(user.getId());
    }

    @Transactional
    public void updateCharacter(CharacterDTO charDTO, String currentUsername) {

        CharacterEntity character = characterRepository.findById(charDTO.id())
                .orElseThrow(() -> new CharacterNotFoundException("Character with ID: " + charDTO.id() + ", not found"));

        if (!character.getOwner().getUsername().equals(currentUsername)) {
            throw new UnauthorizedException("You are not authorized to update this character.");
        }
        character.setName(charDTO.name());
        character.setLevel(charDTO.level());
        characterRepository.save(character);
    }


    public CharacterEntity fetchCharacter(String currentUsername, Long id) {

        CharacterEntity character = characterRepository.findById(id)
                .orElseThrow(() -> new CharacterNotFoundException("Character with ID: " + id + ", not found"));
        if (!character.getOwner().getUsername().equals(currentUsername)) {
            throw new UnauthorizedException("You are not authorized to fetch this character.");
        }
        return character;
    }

}
