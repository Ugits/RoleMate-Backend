package org.jonas.rolemate_backend.api.controller;

import jakarta.validation.Valid;
import org.jonas.rolemate_backend.api.dto.SpellDTO;
import org.jonas.rolemate_backend.api.service.ApiService;
import org.jonas.rolemate_backend.character.model.dto.CharacterLevelDTO;
import org.jonas.rolemate_backend.exception.WebServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/spell")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiController {

    private final ApiService apiService;

    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @PostMapping("/level")
    public Mono<ResponseEntity<List<SpellDTO>>> getSpellsByLevel(
            @Valid @RequestBody CharacterLevelDTO characterLevelDTO) {

        return apiService.fetchSpellsByLevel(characterLevelDTO.level())
                .map(ResponseEntity::ok)
                .onErrorResume(WebServiceException.class, ex ->
                        Mono.just(ResponseEntity.status(ex.getStatusCode()).body(List.of()))
                );
    }

}
