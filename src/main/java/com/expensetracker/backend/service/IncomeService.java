package com.expensetracker.backend.service;

import com.expensetracker.backend.model.Income;
import com.expensetracker.backend.model.User;
import com.expensetracker.backend.repository.IncomeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;

    public IncomeService(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    public Income addIncome(Income income) {
        return incomeRepository.save(income);
    }

    public List<Income> getIncomeByUser(User user) {
        return incomeRepository.findByUser(user);
    }

    public void deleteIncome(Long incomeId) {
        incomeRepository.deleteById(incomeId);
    }

    public BigDecimal getTotalIncome(User user) {
        return incomeRepository.findByUser(user)
                .stream()
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
