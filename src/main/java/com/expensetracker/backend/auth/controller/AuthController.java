package com.expensetracker.backend.auth.controller;

import com.expensetracker.backend.auth.dto.AuthResponseDTO;
import com.expensetracker.backend.auth.dto.LoginRequestDTO;
import com.expensetracker.backend.auth.util.JwtUtil;
import com.expensetracker.backend.model.User;
import com.expensetracker.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // =========================
    // LOGIN (PUBLIC)
    // =========================
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request
    ) {
        User user = userService.authenticate(
                request.getEmail(),
                request.getPassword()
        );

        String token = jwtUtil.generateToken(user.getId());

        return ResponseEntity.ok(
                new AuthResponseDTO(token)
        );
    }
}
