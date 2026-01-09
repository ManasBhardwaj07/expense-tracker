package com.expensetracker.backend.service;

import com.expensetracker.backend.model.Expense;
import com.expensetracker.backend.model.User;
import com.expensetracker.backend.model.enums.ExpenseCategory;
import com.expensetracker.backend.repository.ExpenseRepository;
import com.expensetracker.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseService(
            ExpenseRepository expenseRepository,
            UserRepository userRepository
    ) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    // =========================
    // CREATE
    // =========================
    public Expense createExpense(Long userId, Expense expense) {

        User user = getUserOrFail(userId);

        expense.setId(null);          // enforce new entity
        expense.setUser(user);

        return expenseRepository.save(expense);
    }

    // =========================
    // READ (ALL BY USER)
    // =========================
    public List<Expense> getExpensesByUser(Long userId) {
        return expenseRepository.findByUserId(userId);
    }

    // =========================
    // FILTER BY CATEGORY
    // =========================
    public List<Expense> getExpensesByCategory(
            Long userId,
            ExpenseCategory category
    ) {
        return expenseRepository.findByUserIdAndCategory(userId, category);
    }

    // =========================
    // FILTER BY DATE RANGE
    // =========================
    public List<Expense> getExpensesByDateRange(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        return expenseRepository.findByUserIdAndDateBetween(
                userId,
                startDate,
                endDate
        );
    }

    // =========================
    // UPDATE
    // =========================
    public Expense updateExpense(
            Long expenseId,
            Long userId,
            Expense updatedExpense
    ) {
        Expense existing = expenseRepository.findById(expenseId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Expense not found")
                );

        if (!existing.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized access");
        }

        existing.setDescription(updatedExpense.getDescription());
        existing.setAmount(updatedExpense.getAmount());
        existing.setCategory(updatedExpense.getCategory());
        existing.setDate(updatedExpense.getDate());

        return expenseRepository.save(existing);
    }

    // =========================
    // DELETE
    // =========================
    public void deleteExpense(Long expenseId, Long userId) {

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Expense not found")
                );

        if (!expense.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized access");
        }

        expenseRepository.delete(expense);
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
