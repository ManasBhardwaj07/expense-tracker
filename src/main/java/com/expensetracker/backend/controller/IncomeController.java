package com.expensetracker.backend.controller;

import com.expensetracker.backend.dto.IncomeResponseDTO;
import com.expensetracker.backend.model.Income;
import com.expensetracker.backend.model.enums.IncomeSource;
import com.expensetracker.backend.service.IncomeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    // =========================
    // CREATE
    // =========================
    @PostMapping
    public ResponseEntity<IncomeResponseDTO> createIncome(
            @Valid @RequestBody Income income
    ) {
        Long userId = getCurrentUserId();

        Income created = incomeService.createIncome(userId, income);

        return new ResponseEntity<>(
                toDTO(created),
                HttpStatus.CREATED
        );
    }

    // =========================
    // READ (ALL BY USER)
    // =========================
    @GetMapping
    public ResponseEntity<List<IncomeResponseDTO>> getIncomes() {

        Long userId = getCurrentUserId();

        List<IncomeResponseDTO> response =
                incomeService.getIncomesByUser(userId)
                        .stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // =========================
    // UPDATE
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<IncomeResponseDTO> updateIncome(
            @PathVariable Long id,
            @Valid @RequestBody Income income
    ) {
        Long userId = getCurrentUserId();

        Income updated =
                incomeService.updateIncome(id, userId, income);

        return ResponseEntity.ok(toDTO(updated));
    }

    // =========================
    // DELETE
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {

        Long userId = getCurrentUserId();
        incomeService.deleteIncome(id, userId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<IncomeResponseDTO>> filterIncomes(
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false, defaultValue = "date") String sortBy
    ) {
        Long userId = getCurrentUserId();

        List<Income> incomes;

        if (source != null) {
            incomes = incomeService.getIncomesBySource(
                    userId,
                    IncomeSource.valueOf(source)
            );
        } else if (startDate != null && endDate != null) {
            incomes = incomeService.getIncomesByDateRange(
                    userId,
                    LocalDate.parse(startDate),
                    LocalDate.parse(endDate)
            );
        } else {
            incomes = incomeService.getIncomesByUser(userId);
        }

        incomes.sort((a, b) -> {
            if ("amount".equalsIgnoreCase(sortBy)) {
                return a.getAmount().compareTo(b.getAmount());
            }
            return a.getDate().compareTo(b.getDate());
        });

        return ResponseEntity.ok(
                incomes.stream()
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

    private IncomeResponseDTO toDTO(Income income) {
        return new IncomeResponseDTO(
                income.getId(),
                income.getDescription(),
                income.getAmount(),
                income.getSource(),
                income.getDate()
        );
    }
}
