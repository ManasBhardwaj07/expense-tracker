package com.expensetracker.backend.repository;

import com.expensetracker.backend.model.Expense;
import com.expensetracker.backend.model.enums.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserId(Long userId);

    List<Expense> findByUserIdAndCategory(Long userId, ExpenseCategory category);

    List<Expense> findByUserIdAndDateBetween(
            Long userId,
            LocalDate from,
            LocalDate to
    );

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.user.id = :userId
          AND e.date BETWEEN :from AND :to
    """)
    BigDecimal getTotalExpenseByUserId(
            Long userId,
            LocalDate from,
            LocalDate to
    );
}
