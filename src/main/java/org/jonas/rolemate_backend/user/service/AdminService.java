package org.jonas.rolemate_backend.user.service;

import org.jonas.rolemate_backend.exception.UserAlreadyExistsException;
import org.jonas.rolemate_backend.user.authorites.UserRole;
import org.jonas.rolemate_backend.user.model.dto.SignupRequestDTO;
import org.jonas.rolemate_backend.user.model.dto.UpdateAccountStatusDTO;
import org.jonas.rolemate_backend.user.model.dto.UserCredentialsDTO;
import org.jonas.rolemate_backend.user.model.dto.UsernameDTO;
import org.jonas.rolemate_backend.user.model.entity.CustomUser;
import org.jonas.rolemate_backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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

    public UsernameDTO updateAccountStatus(UpdateAccountStatusDTO updateAccountStatusDTO, Authentication authentication) {

        if ((authentication != null && authentication.isAuthenticated())) {
            if (Objects.equals(authentication.getName(), updateAccountStatusDTO.username())) {
                throw new AccessDeniedException("You cannot disable your own account.");
            }
        }

        CustomUser customUser = userRepository.findByUsername(updateAccountStatusDTO.username())
                .orElseThrow(() -> new UsernameNotFoundException("Username " + updateAccountStatusDTO.username() + " not found"));

        customUser.setEnabled(updateAccountStatusDTO.isEnabled());
        return new UsernameDTO(userRepository.save(customUser).getUsername());
    }

    public UsernameDTO deleteAccount(UsernameDTO user, Authentication authentication) {

        if ((authentication != null && authentication.isAuthenticated())) {
            if (Objects.equals(authentication.getName(), user.username())) {
                throw new AccessDeniedException("You cannot delete your own account.");
            }
        }

        CustomUser customUser = userRepository.findByUsername(user.username())
                .orElseThrow(() -> new UsernameNotFoundException("Username " + user.username() + " not found"));

        userRepository.delete(customUser);
        return new UsernameDTO(customUser.getUsername());
    }
}
