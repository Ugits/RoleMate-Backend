package org.jonas.rolemate_backend.user.service;

import jakarta.validation.Valid;
import org.jonas.rolemate_backend.exception.UserAlreadyExistsException;
import org.jonas.rolemate_backend.user.authorites.UserRole;
import org.jonas.rolemate_backend.user.model.dto.SignupRequestDTO;
import org.jonas.rolemate_backend.user.model.dto.UpdateAccountStatusDTO;
import org.jonas.rolemate_backend.user.model.dto.UserCredentialsDTO;
import org.jonas.rolemate_backend.user.model.entity.CustomUser;
import org.jonas.rolemate_backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Transactional
    public UserCredentialsDTO createAdmin(SignupRequestDTO signupRequestDTO) {

        CustomUser customUser = new CustomUser(
                signupRequestDTO.username(),
                passwordEncoder.encode(signupRequestDTO.password()),
                UserRole.ADMIN,
                true,
                true,
                true,
                true
        );

        if (userRepository.findByUsernameIgnoreCase(customUser.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username " + signupRequestDTO.username() + " is already taken");
        }

        userRepository.save(customUser);
        return new UserCredentialsDTO(customUser.getUsername(),customUser.getPassword());

    }

    public void updateAccountStatus(UpdateAccountStatusDTO updateAccountStatusDTO) {
        CustomUser customUser = userRepository.findByUsername(updateAccountStatusDTO.username())
                .orElseThrow(() -> new UsernameNotFoundException("Username " + updateAccountStatusDTO.username() + " not found"));

        customUser.setEnabled(updateAccountStatusDTO.isEnabled());
        userRepository.save(customUser);
    }

}
