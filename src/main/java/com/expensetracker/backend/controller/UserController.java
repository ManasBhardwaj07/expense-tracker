package com.expensetracker.backend.controller;

import com.expensetracker.backend.dto.UserResponseDTO;
import com.expensetracker.backend.model.User;
import com.expensetracker.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // =========================
    // CREATE USER (PUBLIC)
    // =========================
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody User user
    ) {
        User created = userService.createUser(user);

        UserResponseDTO response = new UserResponseDTO(
                created.getId(),
                created.getName(),
                created.getEmail()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // =========================
    // GET USER BY ID (PROTECTED)
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {

        User user = userService.getUserById(id);

        UserResponseDTO response = new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );

        return ResponseEntity.ok(response);
    }
}
