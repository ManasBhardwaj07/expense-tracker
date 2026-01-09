package com.expensetracker.backend.service;

import com.expensetracker.backend.model.Income;
import com.expensetracker.backend.model.User;
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

    // =========================
    // CREATE
    // =========================
    public Income createIncome(Long userId, Income income) {

        User user = getUserOrFail(userId);

        income.setId(null);          // enforce new entity
        income.setUser(user);

        return incomeRepository.save(income);
    }

    // =========================
    // READ (ALL BY USER)
    // =========================
    public List<Income> getIncomesByUser(Long userId) {
        return incomeRepository.findByUserId(userId);
    }

    // =========================
    // FILTER BY SOURCE
    // =========================
    public List<Income> getIncomesBySource(
            Long userId,
            IncomeSource source
    ) {
        return incomeRepository.findByUserIdAndSource(userId, source);
    }

    // =========================
    // FILTER BY DATE RANGE
    // =========================
    public List<Income> getIncomesByDateRange(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        return incomeRepository.findByUserIdAndDateBetween(
                userId,
                startDate,
                endDate
        );
    }

    // =========================
    // UPDATE
    // =========================
    public Income updateIncome(
            Long incomeId,
            Long userId,
            Income updatedIncome
    ) {
        Income existing = incomeRepository.findById(incomeId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Income not found")
                );

        if (!existing.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized access");
        }

        existing.setAmount(updatedIncome.getAmount());
        existing.setSource(updatedIncome.getSource());
        existing.setDescription(updatedIncome.getDescription());
        existing.setDate(updatedIncome.getDate());

        return incomeRepository.save(existing);
    }

    // =========================
    // DELETE
    // =========================
    public void deleteIncome(Long incomeId, Long userId) {

        Income income = incomeRepository.findById(incomeId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Income not found")
                );

        if (!income.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized access");
        }

        incomeRepository.delete(income);
    }

    // =========================
    // INTERNAL HELPER
    // =========================
    private User getUserOrFail(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found with id: " + userId)
                );
    }
}
