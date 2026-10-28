package com.bank.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fixed_deposits")
public class FixedDeposit {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long fdId;

     @Column(nullable = false, precision = 15, scale = 2)
     private BigDecimal principalAmount;

     @Column(nullable = false)
     private Double interestRate; // e.g., 7.25%

     @Column(nullable = false)
     private Integer durationMonths;

     @Column(nullable = false, precision = 15, scale = 2)
     private BigDecimal maturityAmount;

     @Column(nullable = false)
     private LocalDate maturityDate;

     @Column(nullable = false, length = 20)
     private String status = "ACTIVE"; // ACTIVE, MATURED, PREMATURE_CLOSED

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "account_id", nullable = false)
     private Account linkedAccount;

     public FixedDeposit() {
     }

     public FixedDeposit(BigDecimal principalAmount, Double interestRate, Integer durationMonths,
               BigDecimal maturityAmount, LocalDate maturityDate, Account linkedAccount) {
          this.principalAmount = principalAmount;
          this.interestRate = interestRate;
          this.durationMonths = durationMonths;
          this.maturityAmount = maturityAmount;
          this.maturityDate = maturityDate;
          this.linkedAccount = linkedAccount;
     }
}