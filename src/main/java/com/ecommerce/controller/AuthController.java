package com.ecommerce.controller;

import com.ecommerce.dto.AuthRequest;
import com.ecommerce.dto.AuthResponse;
import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody AuthRequest request) {

        if (
                userRepository.findByEmail(
                                request.getEmail())
                        .isPresent()
        ) {

            return ResponseEntity.badRequest()
                    .body("Email already exists");
        }

        String role =
                request.getRole() != null
                        ? request.getRole()
                        : "CUSTOMER";

        User user = new User(

                request.getEmail(),

                passwordEncoder.encode(
                        request.getPassword()),

                role
        );

        User savedUser =
                userRepository.save(user);

        AuthResponse response =
                new AuthResponse(
                        savedUser.getId(),
                        savedUser.getEmail(),
                        savedUser.getRole());

        return ResponseEntity.ok(response);
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody AuthRequest request) {

        Optional<User> userOpt =
                userRepository.findByEmail(
                        request.getEmail());

        if (userOpt.isPresent()) {

            User user = userOpt.get();

            boolean matches =
                    passwordEncoder.matches(
                            request.getPassword(),
                            user.getPassword());

            if (matches) {

                AuthResponse response =
                        new AuthResponse(
                                user.getId(),
                                user.getEmail(),
                                user.getRole());

                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid credentials");
    }
}