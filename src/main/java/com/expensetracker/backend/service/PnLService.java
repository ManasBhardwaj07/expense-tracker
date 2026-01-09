package com.expensetracker.backend.service;

import com.expensetracker.backend.repository.ExpenseRepository;
import com.expensetracker.backend.repository.IncomeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PnLService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;

    public PnLService(
            IncomeRepository incomeRepository,
            ExpenseRepository expenseRepository
    ) {
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
    }

    // =========================
    // TOTAL PROFIT & LOSS
    // =========================
    public BigDecimal calculateTotalPnL(Long userId) {

        BigDecimal totalIncome = incomeRepository.findByUserId(userId)
                .stream()
                .map(income -> income.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = expenseRepository.findByUserId(userId)
                .stream()
                .map(expense -> expense.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalIncome.subtract(totalExpense);
    }
}
