package com.bank.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long logId;

     @Column(nullable = false, length = 50)
     private String actionType;

     @Column(nullable = false, length = 255)
     private String description;

     @Column(nullable = false, length = 20)
     private String severity;

     private LocalDateTime timestamp = LocalDateTime.now();

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "user_id")
     private User user;

     public AuditLog() {
     }

     public AuditLog(String actionType, String description, String severity, User user) {
          this.actionType = actionType;
          this.description = description;
          this.severity = severity;
          this.user = user;
     }
}