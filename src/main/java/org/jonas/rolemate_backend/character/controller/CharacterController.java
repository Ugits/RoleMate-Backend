package org.jonas.rolemate_backend.character.controller;

import org.jonas.rolemate_backend.character.model.dto.CharacterDTO;
import org.jonas.rolemate_backend.character.model.entity.CharacterEntity;
import org.jonas.rolemate_backend.character.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/character")
public class CharacterController {

    private final CharacterService characterService;

    @Autowired
    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @PostMapping("/create")
    public ResponseEntity<CharacterDTO> createCharacter(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CharacterDTO characterDTO
    ) {
        String currentUsername = userDetails.getUsername();
        CharacterEntity saved = characterService.createCharacter(currentUsername, characterDTO);
        CharacterDTO response = new CharacterDTO(saved.getId(), saved.getName(), saved.getLevel());
        return ResponseEntity.ok(response);
    }

}
