package com.expensetracker.backend.repository;

import com.expensetracker.backend.model.Income;
import com.expensetracker.backend.model.enums.IncomeSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<Income> findByUserId(Long userId);

    List<Income> findByUserIdAndSource(Long userId, IncomeSource source);

    List<Income> findByUserIdAndDateBetween(
            Long userId,
            LocalDate from,
            LocalDate to
    );

    @Query("""
        SELECT COALESCE(SUM(i.amount), 0)
        FROM Income i
        WHERE i.user.id = :userId
          AND i.date BETWEEN :from AND :to
    """)
    BigDecimal getTotalIncomeByUserId(
            Long userId,
            LocalDate from,
            LocalDate to
    );
}
