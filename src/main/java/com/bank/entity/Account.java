package com.bank.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
public class Account {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long accountId;

     @Column(nullable = false, unique = true, length = 20)
     private String accountNumber;

     @Column(nullable = false, length = 20)
     private String accountType; // SAVINGS, CURRENT

     @Column(nullable = false, precision = 15, scale = 2)
     private BigDecimal balance;

     @Column(nullable = false, length = 15)
     private String status = "ACTIVE"; // ACTIVE, FROZEN, CLOSED

     @Version
     private Long version;

     private LocalDateTime createdAt = LocalDateTime.now();

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "user_id", nullable = false)
     private User user;

     public Account() {
     }

     public Account(String accountNumber, String accountType, BigDecimal balance) {
          this.accountNumber = accountNumber;
          this.accountType = accountType;
          this.balance = balance;
     }

     // Getters and Setters
     public Long getAccountId() {
          return accountId;
     }

     public String getAccountNumber() {
          return accountNumber;
     }

     public BigDecimal getBalance() {
          return balance;
     }

     public void setBalance(BigDecimal balance) {
          this.balance = balance;
     }

     public User getUser() {
          return user;
     }

     public void setUser(User user) {
          this.user = user;
     }
}