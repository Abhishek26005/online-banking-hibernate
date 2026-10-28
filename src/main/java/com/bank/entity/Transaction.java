package com.bank.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long transactionId;

     @Column(nullable = false, unique = true, length = 40)
     private String referenceNumber = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

     @Column(nullable = false, length = 20)
     private String transactionType; // DEPOSIT, WITHDRAWAL, TRANSFER

     @Column(nullable = false, precision = 15, scale = 2)
     private BigDecimal amount;

     private LocalDateTime timestamp = LocalDateTime.now();

     @Column(length = 255)
     private String remarks;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "source_account_id")
     private Account sourceAccount;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "target_account_id")
     private Account targetAccount;

     public Transaction() {
     }

     public Transaction(String transactionType, BigDecimal amount, Account sourceAccount, Account targetAccount,
               String remarks) {
          this.transactionType = transactionType;
          this.amount = amount;
          this.sourceAccount = sourceAccount;
          this.targetAccount = targetAccount;
          this.remarks = remarks;
     }

     public String getReferenceNumber() {
          return referenceNumber;
     }
}