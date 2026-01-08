package com.expensetracker.backend.controller;

import com.expensetracker.backend.dto.ExpenseResponseDTO;
import com.expensetracker.backend.model.Expense;
import com.expensetracker.backend.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // =========================
    // CREATE
    // =========================
    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> createExpense(
            @Valid @RequestBody Expense expense
    ) {
        Long userId = getCurrentUserId();

        Expense created = expenseService.createExpense(userId, expense);

        return new ResponseEntity<>(
                toDTO(created),
                HttpStatus.CREATED
        );
    }

    // =========================
    // READ (all by user)
    // =========================
    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> getExpenses() {

        Long userId = getCurrentUserId();

        List<ExpenseResponseDTO> response =
                expenseService.getExpensesByUser(userId)
                        .stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // =========================
    // UPDATE
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody Expense expense
    ) {
        Long userId = getCurrentUserId();

        Expense updated =
                expenseService.updateExpense(id, userId, expense);

        return ResponseEntity.ok(toDTO(updated));
    }

    // =========================
    // DELETE
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {

        Long userId = getCurrentUserId();
        expenseService.deleteExpense(id, userId);

        return ResponseEntity.noContent().build();
    }

    // =========================
    // HELPERS
    // =========================
    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private ExpenseResponseDTO toDTO(Expense expense) {
        return new ExpenseResponseDTO(
                expense.getId(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getDate()
        );
    }
}
