package com.expensetracker.backend.repository;

import com.expensetracker.backend.model.Income;
import com.expensetracker.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<Income> findByUser(User user);
}
