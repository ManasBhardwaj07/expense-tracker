package com.expensetracker.backend.service;

import com.expensetracker.backend.repository.ExpenseRepository;
import com.expensetracker.backend.repository.IncomeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class PnLService {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;

    public PnLService(
            ExpenseRepository expenseRepository,
            IncomeRepository incomeRepository
    ) {
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
    }

    public BigDecimal calculatePnL(
            Long userId,
            LocalDate from,
            LocalDate to
    ) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("From date cannot be after To date");
        }

        BigDecimal totalIncome =
                incomeRepository.getTotalIncomeByUserId(userId, from, to);

        BigDecimal totalExpense =
                expenseRepository.getTotalExpenseByUserId(userId, from, to);

        return totalIncome.subtract(totalExpense);
    }
}
