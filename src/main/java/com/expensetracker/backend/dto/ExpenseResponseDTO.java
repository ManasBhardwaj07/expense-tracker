package com.expensetracker.backend.dto;

import com.expensetracker.backend.model.enums.ExpenseCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseResponseDTO {

    private Long id;
    private String description;
    private BigDecimal amount;
    private ExpenseCategory category;
    private LocalDate date;

    public ExpenseResponseDTO(
            Long id,
            String description,
            BigDecimal amount,
            ExpenseCategory category,
            LocalDate date
    ) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    // =========================
    // GETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }
}
