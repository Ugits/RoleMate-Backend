package org.jonas.rolemate_backend.character.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.jonas.rolemate_backend.character.model.dto.CharacterDTO;
import org.jonas.rolemate_backend.character.model.dto.CharacterIdDTO;
import org.jonas.rolemate_backend.character.model.entity.CharacterEntity;
import org.jonas.rolemate_backend.character.repository.CharacterRepository;
import org.jonas.rolemate_backend.user.authorites.UserRole;
import org.jonas.rolemate_backend.user.model.entity.CustomUser;
import org.jonas.rolemate_backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CharacterControllerTest {


    private final MockMvc mockMvc;
    private final UserRepository userRepository;
    private final CharacterRepository characterRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CharacterControllerTest(MockMvc mockMvc,
                                   UserRepository userRepository,
                                   CharacterRepository characterRepository,
                                   ObjectMapper objectMapper,
                                   PasswordEncoder passwordEncoder) {
        this.mockMvc = mockMvc;
        this.userRepository = userRepository;
        this.characterRepository = characterRepository;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
    }

    private CharacterEntity sampleCharacter;

    @BeforeEach
    void setUp() {
        CustomUser user = new CustomUser(
                "John",
                passwordEncoder.encode("123"),
                UserRole.USER,
                true,
                true,
                true,
                true
        );
        userRepository.save(user);

        sampleCharacter = new CharacterEntity();
        sampleCharacter.setName("Aragorn");
        sampleCharacter.setLevel(10);
        sampleCharacter.setStrength(18);
        sampleCharacter.setDexterity(15);
        sampleCharacter.setConstitution(17);
        sampleCharacter.setIntelligence(14);
        sampleCharacter.setWisdom(16);
        sampleCharacter.setCharisma(20);
        sampleCharacter.setOwner(user);

        sampleCharacter = characterRepository.save(sampleCharacter);
    }

    @Test
    @DisplayName("POST /character/create - Success for USER")
    @WithMockUser(username = "John", roles = "USER")
    void createCharacter_SuccessForUser() throws Exception {
        CharacterDTO inputDTO = new CharacterDTO(
                null,
                "Legolas",
                8,
                16,
                20,
                14,
                18,
                15,
                17
        );

        mockMvc.perform(post("/character/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Legolas"))
                .andExpect(jsonPath("$.level").value(8))
                .andExpect(jsonPath("$.strength").value(16))
                .andExpect(jsonPath("$.dexterity").value(20))
                .andExpect(jsonPath("$.constitution").value(14))
                .andExpect(jsonPath("$.intelligence").value(18))
                .andExpect(jsonPath("$.wisdom").value(15))
                .andExpect(jsonPath("$.charisma").value(17));

    }


    @Test
    @DisplayName("DELETE /character/delete - Success for USER")
    @WithMockUser(username = "John", roles = "USER")
    void deleteCharacter_SuccessForUser() throws Exception {
        CharacterIdDTO deleteRequest = new CharacterIdDTO(sampleCharacter.getId());

        mockMvc.perform(delete("/character/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(status().isNoContent());

        boolean exists = characterRepository.existsById(sampleCharacter.getId());
        assertFalse(exists, "Character should be deleted from the repository");

    }

    @Test
    @DisplayName("PUT /character/update - Success for USER")
    @WithMockUser(username = "John", roles = "USER")
    void updateCharacter_SuccessForUser() throws Exception {

        CharacterDTO updatedDTO = new CharacterDTO(
                sampleCharacter.getId(),
                "Aragorn II",
                12,
                19,
                17,
                19,
                16,
                18,
                21
        );

        mockMvc.perform(put("/character/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedDTO.id()))
                .andExpect(jsonPath("$.name").value("Aragorn II"))
                .andExpect(jsonPath("$.level").value(12))
                .andExpect(jsonPath("$.strength").value(19))
                .andExpect(jsonPath("$.dexterity").value(17))
                .andExpect(jsonPath("$.constitution").value(19))
                .andExpect(jsonPath("$.intelligence").value(16))
                .andExpect(jsonPath("$.wisdom").value(18))
                .andExpect(jsonPath("$.charisma").value(21));


        CharacterEntity updatedCharacter = characterRepository.findById(sampleCharacter.getId()).orElse(null);
        assertTrue(updatedCharacter != null && "Aragorn II".equals(updatedCharacter.getName()), "Character should be updated in the repository");
        assertEquals(12, updatedCharacter.getLevel(), "Character level should be updated to 12");
        assertEquals(19, updatedCharacter.getStrength(), "Character strength should be updated to 19");
        assertEquals(17, updatedCharacter.getDexterity(), "Character dexterity should be updated to 17");
        assertEquals(19, updatedCharacter.getConstitution(), "Character constitution should be updated to 19");
        assertEquals(16, updatedCharacter.getIntelligence(), "Character intelligence should be updated to 16");
        assertEquals(18, updatedCharacter.getWisdom(), "Character wisdom should be updated to 18");
        assertEquals(21, updatedCharacter.getCharisma(), "Character charisma should be updated to 21");
    }

    @Test
    @DisplayName("POST /character/fetch - Success for USER")
    @WithMockUser(username = "John", roles = "USER")
    void fetchCharacter_SuccessForUser() throws Exception {

        CharacterIdDTO fetchRequest = new CharacterIdDTO(sampleCharacter.getId());

        mockMvc.perform(post("/character/fetch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fetchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleCharacter.getId()))
                .andExpect(jsonPath("$.name").value(sampleCharacter.getName()))
                .andExpect(jsonPath("$.level").value(sampleCharacter.getLevel()))
                .andExpect(jsonPath("$.strength").value(sampleCharacter.getStrength()))
                .andExpect(jsonPath("$.dexterity").value(sampleCharacter.getDexterity()))
                .andExpect(jsonPath("$.constitution").value(sampleCharacter.getConstitution()))
                .andExpect(jsonPath("$.intelligence").value(sampleCharacter.getIntelligence()))
                .andExpect(jsonPath("$.wisdom").value(sampleCharacter.getWisdom()))
                .andExpect(jsonPath("$.charisma").value(sampleCharacter.getCharisma()));
    }
}