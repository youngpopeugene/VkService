package com.youngpopeugene.vkservice.services;

import com.youngpopeugene.vkservice.config.JwtService;
import com.youngpopeugene.vkservice.model.auth.AuthenticationRequest;
import com.youngpopeugene.vkservice.model.auth.AuthenticationResponse;
import com.youngpopeugene.vkservice.model.auth.RegisterRequest;
import com.youngpopeugene.vkservice.model.user.Role;
import com.youngpopeugene.vkservice.model.user.User;
import com.youngpopeugene.vkservice.repository.UserRepository;
import com.youngpopeugene.vkservice.util.Validator;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var isUserAlreadyExists = repository.findByEmail(request.getEmail()).stream().findAny().isPresent();
        if (isUserAlreadyExists)
            throw new ValidationException ("Validation error: user with this email already exists");

        ArrayList<String> errors = Validator.validateEmail(request.getEmail());
        errors.addAll(Validator.validatePassword(request.getPassword()));
        if (!errors.isEmpty())
            throw new ValidationException(Validator.prepareValidationErrorMessage(errors));

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}

