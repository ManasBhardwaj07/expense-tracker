package com.expensetracker.backend.controller;

import com.expensetracker.backend.model.Expense;
import com.expensetracker.backend.model.User;
import com.expensetracker.backend.service.ExpenseService;
import com.expensetracker.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;

    public ExpenseController(ExpenseService expenseService, UserService userService) {
        this.expenseService = expenseService;
        this.userService = userService;
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Expense> addExpense(
            @PathVariable Long userId,
            @RequestBody Expense expense) {

        User user = userService.getUserById(userId);
        expense.setUser(user);

        Expense saved = expenseService.addExpense(expense);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Expense>> getExpenses(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(expenseService.getExpensesByUser(user));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long expenseId) {
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.noContent().build();
    }
}
