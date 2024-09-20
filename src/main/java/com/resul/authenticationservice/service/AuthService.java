package com.resul.authenticationservice.service;

import com.resul.authenticationservice.auth.JwtService;
import com.resul.authenticationservice.dto.AuthenticationRequestDTO;
import com.resul.authenticationservice.dto.AuthenticationResponseDTO;
import com.resul.authenticationservice.dto.RegisterRequestDTO;
import com.resul.authenticationservice.exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserServiceClient userServiceClient;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDTO register(RegisterRequestDTO request) {
        String secretToken = "mySecretToken123";
        AuthenticationResponseDTO createdUser = userServiceClient.createUser(secretToken, request);
        String jwtToken = createdUser.getToken();
        return AuthenticationResponseDTO.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new AuthenticationException("Invalid username or password");
        }

        var user = userServiceClient.getUserByUsername(request.getUsername());
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDTO.builder()
                .token(jwtToken)
                .build();
    }
}