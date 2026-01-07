package com.expensetracker.backend.controller;

import com.expensetracker.backend.model.Income;
import com.expensetracker.backend.model.enums.IncomeSource;
import com.expensetracker.backend.service.IncomeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Income> createIncome(
            @PathVariable Long userId,
            @RequestBody Income income
    ) {
        return ResponseEntity.ok(incomeService.createIncome(userId, income));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Income>> getByUser(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(incomeService.getByUser(userId));
    }

    @GetMapping("/user/{userId}/source/{source}")
    public ResponseEntity<List<Income>> getBySource(
            @PathVariable Long userId,
            @PathVariable IncomeSource source
    ) {
        return ResponseEntity.ok(incomeService.getBySource(userId, source));
    }

    @GetMapping("/user/{userId}/date")
    public ResponseEntity<List<Income>> getByDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(
                incomeService.getByDateRange(userId, from, to)
        );
    }
}
