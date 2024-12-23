package org.jonas.rolemate_backend.character.controller;

import org.jonas.rolemate_backend.character.model.dto.CharacterDTO;
import org.jonas.rolemate_backend.character.model.dto.DeleteCharacterRequestDTO;
import org.jonas.rolemate_backend.character.model.entity.CharacterEntity;
import org.jonas.rolemate_backend.character.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCharacter(@RequestBody DeleteCharacterRequestDTO deleteCharacterRequestDTO) {
        characterService.deleteCharacter(deleteCharacterRequestDTO.id());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/fetch-all")
    public ResponseEntity<List<CharacterDTO>> fetchAllCharacters(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String currentUsername = userDetails.getUsername();
        List<CharacterEntity> characterList = characterService.getAllCharacters(currentUsername);
        List<CharacterDTO> response = characterList.stream()
                .map(character ->
                        new CharacterDTO(
                                character.getId(),
                                character.getName(),
                                character.getLevel())
                )
                .toList();
        return ResponseEntity.ok().body(response);
    }

}
