package com.bank.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "kyc_documents")
public class KycDocument {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long kycId;

     @Column(nullable = false, unique = true, length = 10)
     private String panNumber;

     @Column(nullable = false, unique = true, length = 12)
     private String aadhaarNumber;

     @Column(nullable = false, length = 255)
     private String residentialAddress;

     @Column(nullable = false, length = 20)
     private String verificationStatus = "PENDING";

     private String verifiedByAdmin;
     private LocalDateTime verifiedAt;

     @OneToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "user_id", nullable = false, unique = true)
     private User user;

     public KycDocument() {
     }

     public KycDocument(String panNumber, String aadhaarNumber, String residentialAddress, User user) {
          this.panNumber = panNumber;
          this.aadhaarNumber = aadhaarNumber;
          this.residentialAddress = residentialAddress;
          this.user = user;
     }

     public String getVerificationStatus() {
          return verificationStatus;
     }

     public void setVerificationStatus(String status) {
          this.verificationStatus = status;
     }

     public void setVerifiedByAdmin(String admin) {
          this.verifiedByAdmin = admin;
     }

     public void setVerifiedAt(LocalDateTime time) {
          this.verifiedAt = time;
     }
}