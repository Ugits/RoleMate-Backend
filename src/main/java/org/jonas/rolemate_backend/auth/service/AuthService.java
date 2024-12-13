package org.jonas.rolemate_backend.auth.service;

import org.jonas.rolemate_backend.auth.dto.AuthRequest;
import org.jonas.rolemate_backend.auth.dto.AuthResponse;
import org.jonas.rolemate_backend.auth.jwt.JWTService;
import org.jonas.rolemate_backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JWTService jwtService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public AuthResponse verify(AuthRequest authRequest) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authRequest.username(),
                                authRequest.password()
                        ));

        String generatedToken = jwtService.generateToken(authRequest.username());
        System.out.println("Generated Token: " + generatedToken);

        return
                new AuthResponse(
                        generatedToken,
                        authentication.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .filter(authority -> authority.startsWith("ROLE_"))
                                .findFirst()
                                .orElseThrow(() -> new IllegalStateException("User has no role"))
                                .substring(5)
                );
    }

}
