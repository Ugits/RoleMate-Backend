package org.jonas.rolemate_backend.user.controller;

import jakarta.validation.Valid;
import org.jonas.rolemate_backend.user.model.dto.SignupRequestDTO;
import org.jonas.rolemate_backend.user.model.dto.UserCredentialsDTO;
import org.jonas.rolemate_backend.user.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        return adminService.createAdmin(signupRequestDTO);
    }

    

}
