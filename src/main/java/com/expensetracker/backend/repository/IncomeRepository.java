package com.expensetracker.backend.repository;

import com.expensetracker.backend.model.Income;
import com.expensetracker.backend.model.enums.IncomeSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<Income> findByUserId(Long userId);

    List<Income> findByUserIdAndSource(Long userId, IncomeSource source);

    List<Income> findByUserIdAndDateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );
}
