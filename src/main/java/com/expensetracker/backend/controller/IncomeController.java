package com.expensetracker.backend.controller;

import com.expensetracker.backend.model.Income;
import com.expensetracker.backend.model.User;
import com.expensetracker.backend.service.IncomeService;
import com.expensetracker.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/income")
public class IncomeController {

    private final IncomeService incomeService;
    private final UserService userService;

    public IncomeController(IncomeService incomeService, UserService userService) {
        this.incomeService = incomeService;
        this.userService = userService;
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Income> addIncome(
            @PathVariable Long userId,
            @RequestBody Income income) {

        User user = userService.getUserById(userId);
        income.setUser(user);

        Income saved = incomeService.addIncome(income);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Income>> getIncome(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(incomeService.getIncomeByUser(user));
    }

    @DeleteMapping("/{incomeId}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long incomeId) {
        incomeService.deleteIncome(incomeId);
        return ResponseEntity.noContent().build();
    }
}
