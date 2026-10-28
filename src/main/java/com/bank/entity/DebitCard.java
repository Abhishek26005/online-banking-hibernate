package com.bank.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "debit_cards")
public class DebitCard {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long cardId;

     @Column(nullable = false, unique = true, length = 16)
     private String cardNumber;

     @Column(nullable = false)
     private LocalDate expiryDate;

     @Column(nullable = false, length = 64)
     private String pinHash; // SHA-256 hashed PIN

     @Column(nullable = false, precision = 15, scale = 2)
     private BigDecimal dailyLimit = new BigDecimal("50000.00");

     @Column(nullable = false)
     private Boolean isBlocked = false;

     @OneToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "account_id", nullable = false, unique = true)
     private Account account;

     public DebitCard() {
     }

     public DebitCard(String cardNumber, LocalDate expiryDate, String pinHash, Account account) {
          this.cardNumber = cardNumber;
          this.expiryDate = expiryDate;
          this.pinHash = pinHash;
          this.account = account;
     }

     public Boolean getIsBlocked() {
          return isBlocked;
     }

     public void setIsBlocked(Boolean blocked) {
          this.isBlocked = blocked;
     }

     public BigDecimal getDailyLimit() {
          return dailyLimit;
     }
}