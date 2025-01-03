package org.jonas.rolemate_backend.user.controller;

import jakarta.validation.Valid;
import org.jonas.rolemate_backend.user.model.dto.SignupRequestDTO;
import org.jonas.rolemate_backend.user.model.dto.UpdateAccountStatusDTO;
import org.jonas.rolemate_backend.user.model.dto.UserCredentialsDTO;
import org.jonas.rolemate_backend.user.model.dto.UsernameDTO;
import org.jonas.rolemate_backend.user.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserCredentialsDTO> register(@RequestBody @Valid SignupRequestDTO signupRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createAdmin(signupRequestDTO));
    }

    @PatchMapping("/user/status")
    public ResponseEntity<UsernameDTO> updateStatus(@RequestBody @Valid UpdateAccountStatusDTO updateAccountStatusDTO, Authentication authentication) {
        return ResponseEntity.ok().body(adminService.updateAccountStatus(updateAccountStatusDTO, authentication));
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<UsernameDTO> deleteUser(@RequestBody @Valid UsernameDTO user, Authentication authentication) {
        return ResponseEntity.ok().body(adminService.deleteAccount(user, authentication));
    }


}
