package com.expensetracker.backend.model;

import com.expensetracker.backend.model.enums.IncomeSource;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "income")
@Getter
@Setter
@NoArgsConstructor
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncomeSource source;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    public Income(String description,
                  BigDecimal amount,
                  IncomeSource source,
                  LocalDate date,
                  User user) {
        this.description = description;
        this.amount = amount;
        this.source = source;
        this.date = date;
        this.user = user;
    }
}
