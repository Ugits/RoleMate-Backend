package org.jonas.rolemate_backend.user.controller;

import jakarta.validation.Valid;
import org.jonas.rolemate_backend.user.model.dto.SignupRequestDTO;
import org.jonas.rolemate_backend.user.model.dto.UserCredentialsDTO;
import org.jonas.rolemate_backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<UserCredentialsDTO> register(@RequestBody @Valid SignupRequestDTO signupRequestDTO) {
        return userService.createUser(signupRequestDTO);
    }

    @GetMapping("/deleteMe")
    public ResponseEntity<UserCredentialsDTO> deleteMe(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userService.deleteAuthenticatedUser(authentication));
    }


}
