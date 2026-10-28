package com.bank.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long userId;

     @Column(nullable = false, length = 100)
     private String fullName;

     @Column(nullable = false, unique = true, length = 100)
     private String email;

     @Column(nullable = false, unique = true, length = 15)
     private String phone;

     @Column(nullable = false)
     private String passwordHash;

     @Column(nullable = false, length = 20)
     private String role; // CUSTOMER, ADMIN

     private LocalDateTime createdAt = LocalDateTime.now();

     @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
     private List<Account> accounts = new ArrayList<>();

     public User() {
     }

     public User(String fullName, String email, String phone, String passwordHash, String role) {
          this.fullName = fullName;
          this.email = email;
          this.phone = phone;
          this.passwordHash = passwordHash;
          this.role = role;
     }

     public Long getUserId() {
          return userId;
     }

     public String getFullName() {
          return fullName;
     }

     public List<Account> getAccounts() {
          return accounts;
     }

     public void addAccount(Account account) {
          accounts.add(account);
          account.setUser(this);
     }
}