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

    // Create expense for a user
    public Expense createExpense(Long userId, Expense expense) {

        User user = getUserOrFail(userId);
        expense.setUser(user);

        return expenseRepository.save(expense);
    }

    // Get all expenses of a user
    public List<Expense> getExpensesByUser(Long userId) {
        return expenseRepository.findByUserId(userId);
    }

    // Filter by category
    public List<Expense> getExpensesByCategory(
            Long userId,
            ExpenseCategory category
    ) {
        return expenseRepository.findByUserIdAndCategory(userId, category);
    }

    // Filter by date range
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

    // Internal helper
    private User getUserOrFail(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found with id: " + userId)
                );
    }
}
