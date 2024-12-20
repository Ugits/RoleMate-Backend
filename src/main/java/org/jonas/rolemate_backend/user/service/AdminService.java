package org.jonas.rolemate_backend.user.service;

import org.jonas.rolemate_backend.exception.UserAlreadyExistsException;
import org.jonas.rolemate_backend.user.authorites.UserRole;
import org.jonas.rolemate_backend.user.model.dto.SignupRequestDTO;
import org.jonas.rolemate_backend.user.model.dto.UserCredentialsDTO;
import org.jonas.rolemate_backend.user.model.entity.CustomUser;
import org.jonas.rolemate_backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


}
