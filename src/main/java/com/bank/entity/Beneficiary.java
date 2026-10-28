package com.bank.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "beneficiaries")
public class Beneficiary {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long beneficiaryId;

     @Column(nullable = false, length = 100)
     private String beneficiaryName;

     @Column(nullable = false, length = 20)
     private String accountNumber;

     @Column(nullable = false, length = 15)
     private String ifscCode;

     @Column(nullable = false, length = 50)
     private String bankName;

     @Column(nullable = false, precision = 15, scale = 2)
     private BigDecimal maxTransferLimit;

     private LocalDateTime addedAt = LocalDateTime.now();

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "user_id", nullable = false)
     private User user;

     public Beneficiary() {
     }

     public Beneficiary(String beneficiaryName, String accountNumber, String ifscCode, String bankName,
               BigDecimal maxTransferLimit, User user) {
          this.beneficiaryName = beneficiaryName;
          this.accountNumber = accountNumber;
          this.ifscCode = ifscCode;
          this.bankName = bankName;
          this.maxTransferLimit = maxTransferLimit;
          this.user = user;
     }

     public BigDecimal getMaxTransferLimit() {
          return maxTransferLimit;
     }

     public String getAccountNumber() {
          return accountNumber;
     }
}