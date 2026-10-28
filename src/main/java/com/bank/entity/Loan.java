package com.bank.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
public class Loan {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long loanId;

     @Column(nullable = false, length = 30)
     private String loanType; // HOME, EDUCATION, PERSONAL

     @Column(nullable = false, precision = 15, scale = 2)
     private BigDecimal principalAmount;

     @Column(nullable = false)
     private Double interestRate;

     @Column(nullable = false)
     private Integer tenureMonths;

     @Column(nullable = false, length = 20)
     private String status = "PENDING"; // PENDING, APPROVED, REJECTED

     private LocalDateTime appliedAt = LocalDateTime.now();

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "user_id", nullable = false)
     private User user;

     public Loan() {
     }

     public Loan(String loanType, BigDecimal principalAmount, Double interestRate, Integer tenureMonths, User user) {
          this.loanType = loanType;
          this.principalAmount = principalAmount;
          this.interestRate = interestRate;
          this.tenureMonths = tenureMonths;
          this.user = user;
     }
}