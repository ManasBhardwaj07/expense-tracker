package com.expensetracker.backend.service;

import com.expensetracker.backend.model.Income;
import com.expensetracker.backend.model.enums.IncomeSource;
import com.expensetracker.backend.repository.IncomeRepository;
import com.expensetracker.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    public IncomeService(
            IncomeRepository incomeRepository,
            UserRepository userRepository
    ) {
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
    }

    public Income createIncome(Long userId, Income income) {
        income.setUser(
                userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"))
        );
        return incomeRepository.save(income);
    }

    public List<Income> getByUser(Long userId) {
        return incomeRepository.findByUserId(userId);
    }

    public List<Income> getBySource(Long userId, IncomeSource source) {
        return incomeRepository.findByUserIdAndSource(userId, source);
    }

    public List<Income> getByDateRange(Long userId, LocalDate from, LocalDate to) {
        return incomeRepository.findByUserIdAndDateBetween(userId, from, to);
    }
}
