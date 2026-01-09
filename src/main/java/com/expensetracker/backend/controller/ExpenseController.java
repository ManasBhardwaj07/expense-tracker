package com.expensetracker.backend.controller;

import com.expensetracker.backend.dto.ExpenseResponseDTO;
import com.expensetracker.backend.model.Expense;
import com.expensetracker.backend.model.enums.ExpenseCategory;
import com.expensetracker.backend.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    // READ (ALL BY USER)
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


    @GetMapping("/filter")
    public ResponseEntity<List<ExpenseResponseDTO>> filterExpenses(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false, defaultValue = "date") String sortBy
    ) {
        Long userId = getCurrentUserId();

        List<Expense> expenses;

        if (category != null) {
            expenses = expenseService.getExpensesByCategory(
                    userId,
                    ExpenseCategory.valueOf(category)
            );
        } else if (startDate != null && endDate != null) {
            expenses = expenseService.getExpensesByDateRange(
                    userId,
                    LocalDate.parse(startDate),
                    LocalDate.parse(endDate)
            );
        } else {
            expenses = expenseService.getExpensesByUser(userId);
        }

        expenses.sort((a, b) -> {
            if ("amount".equalsIgnoreCase(sortBy)) {
                return a.getAmount().compareTo(b.getAmount());
            }
            return a.getDate().compareTo(b.getDate());
        });

        return ResponseEntity.ok(
                expenses.stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList())
        );
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
