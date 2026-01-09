package com.expensetracker.backend.dto;

import com.expensetracker.backend.model.enums.IncomeSource;

import java.math.BigDecimal;
import java.time.LocalDate;

public class IncomeResponseDTO {

    private Long id;
    private String description;
    private BigDecimal amount;
    private IncomeSource source;
    private LocalDate date;

    public IncomeResponseDTO(
            Long id,
            String description,
            BigDecimal amount,
            IncomeSource source,
            LocalDate date
    ) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.source = source;
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

    public IncomeSource getSource() {
        return source;
    }

    public LocalDate getDate() {
        return date;
    }
}
