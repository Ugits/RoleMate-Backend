package org.jonas.rolemate_backend.character.service;

import org.jonas.rolemate_backend.character.model.dto.CharacterDTO;
import org.jonas.rolemate_backend.character.model.entity.CharacterEntity;
import org.jonas.rolemate_backend.character.repository.CharacterRepository;
import org.jonas.rolemate_backend.user.model.entity.CustomUser;
import org.jonas.rolemate_backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

}
