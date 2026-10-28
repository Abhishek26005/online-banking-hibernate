package com.bank.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "emi_schedules")
public class EmiSchedule {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long emiId;

     @Column(nullable = false)
     private Integer installmentNumber;

     @Column(nullable = false)
     private LocalDate dueDate;

     @Column(nullable = false, precision = 15, scale = 2)
     private BigDecimal emiAmount;

     @Column(nullable = false, length = 15)
     private String paymentStatus = "UNPAID"; // UNPAID, PAID, OVERDUE

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "loan_id", nullable = false)
     private Loan loan;

     public EmiSchedule() {
     }

     public EmiSchedule(Integer installmentNumber, LocalDate dueDate, BigDecimal emiAmount, Loan loan) {
          this.installmentNumber = installmentNumber;
          this.dueDate = dueDate;
          this.emiAmount = emiAmount;
          this.loan = loan;
     }

     public String getPaymentStatus() {
          return paymentStatus;
     }

     public void setPaymentStatus(String status) {
          this.paymentStatus = status;
     }

     public BigDecimal getEmiAmount() {
          return emiAmount;
     }
}