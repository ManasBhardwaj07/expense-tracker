package com.expensetracker.backend.controller;

import com.expensetracker.backend.dto.ExpenseResponseDTO;
import com.expensetracker.backend.model.Expense;
import com.expensetracker.backend.model.enums.ExpenseCategory;
import com.expensetracker.backend.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // 1️⃣ Create Expense
    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> createExpense(
            @PathVariable Long userId,
            @Valid @RequestBody Expense expense
    ) {
        Expense created = expenseService.createExpense(userId, expense);
        return new ResponseEntity<>(mapToDTO(created), HttpStatus.CREATED);
    }

    // 2️⃣ Get all expenses of a user
    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> getExpenses(
            @PathVariable Long userId
    ) {
        List<ExpenseResponseDTO> response = expenseService
                .getExpensesByUser(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // 3️⃣ Filter expenses
    @GetMapping("/filter")
    public ResponseEntity<List<ExpenseResponseDTO>> filterExpenses(
            @PathVariable Long userId,
            @RequestParam(required = false) ExpenseCategory category,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {

        List<Expense> expenses;

        if (category != null) {
            expenses = expenseService.getExpensesByCategory(userId, category);
        } else if (startDate != null && endDate != null) {
            expenses = expenseService.getExpensesByDateRange(userId, startDate, endDate);
        } else {
            return ResponseEntity.badRequest().build();
        }

        List<ExpenseResponseDTO> response = expenses
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // 🔒 Private mapper (controller responsibility)
    private ExpenseResponseDTO mapToDTO(Expense expense) {
        return new ExpenseResponseDTO(
                expense.getId(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getDate()
        );
    }
}
