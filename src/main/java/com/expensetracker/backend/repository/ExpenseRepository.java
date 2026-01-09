package com.expensetracker.backend.repository;

import com.expensetracker.backend.model.Expense;
import com.expensetracker.backend.model.enums.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserId(Long userId);

    List<Expense> findByUserIdAndCategory(Long userId, ExpenseCategory category);

    List<Expense> findByUserIdAndDateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );
}
