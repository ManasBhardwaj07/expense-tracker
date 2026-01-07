package com.expensetracker.backend.model;

import com.expensetracker.backend.model.enums.ExpenseCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String description;


    @DecimalMin(value = "0.01", inclusive = true)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;


    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseCategory category;

    @NotNull
    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Expense(String description,BigDecimal amount,ExpenseCategory category,LocalDate date,User user) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.user = user;
    }
}
