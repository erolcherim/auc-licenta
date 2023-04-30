package com.unibuc.auclicenta.service;

import com.unibuc.auclicenta.controller.auth.AuthenticationRequest;
import com.unibuc.auclicenta.controller.auth.AuthenticationResponse;
import com.unibuc.auclicenta.controller.auth.RegisterRequest;
import com.unibuc.auclicenta.data.users.Role;
import com.unibuc.auclicenta.data.users.User;
import com.unibuc.auclicenta.exception.AuthFailedException;
import com.unibuc.auclicenta.exception.InvalidPasswordException;
import com.unibuc.auclicenta.exception.PasswordsDoNotMatchException;
import com.unibuc.auclicenta.exception.UserAlreadyExistsException;
import com.unibuc.auclicenta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        var user = User.builder()
                .firstName((registerRequest.getFirstName()))
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException();
        } else if (!registerRequest.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            throw new InvalidPasswordException();
        } else if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        } else {
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user); //TODO remove
            return AuthenticationResponse
                    .builder()
                    .token(jwtToken)
                    .build();
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new AuthFailedException();
        }

        var user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(authenticationRequest.getEmail()));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }
}