package com.bank;

import com.bank.config.HibernateUtil;
import com.bank.entity.Account;
import com.bank.entity.User;
import com.bank.service.BankingService;

import java.math.BigDecimal;

public class App {
    public static void main(String[] args) {
        BankingService bankingService = new BankingService();

        User u1 = new User("Rahul Patil", "rahul@example.com", "9876543210", "hashed_pw_1", "CUSTOMER");
        Account a1 = new Account("SBIN000101", "SAVINGS", new BigDecimal("50000.00"));

        User u2 = new User("Sneha Kulkarni", "sneha@example.com", "9876543211", "hashed_pw_2", "CUSTOMER");
        Account a2 = new Account("SBIN000102", "SAVINGS", new BigDecimal("15000.00"));

        bankingService.registerUserWithAccount(u1, a1);
        bankingService.registerUserWithAccount(u2, a2);

        bankingService.transferFunds(
                "SBIN000101",
                "SBIN000102",
                new BigDecimal("12500.00"),
                "Rent Payment");

        HibernateUtil.shutdown();
    }
}