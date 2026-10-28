package com.bank.service;

import com.bank.config.HibernateUtil;
import com.bank.entity.Account;
import com.bank.entity.AuditLog;
import com.bank.entity.DebitCard;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;

public class SecurityAndCardService {

     private static final BigDecimal FRAUD_THRESHOLD = new BigDecimal("100000.00");

     public void processAtmWithdrawal(String cardNumber, BigDecimal amount) {
          Transaction tx = null;
          try (Session session = HibernateUtil.getSessionFactory().openSession()) {
               tx = session.beginTransaction();

               DebitCard card = session.createQuery("FROM DebitCard WHERE cardNumber = :num", DebitCard.class)
                         .setParameter("num", cardNumber)
                         .uniqueResult();

               if (card == null || card.getIsBlocked()) {
                    throw new SecurityException("Card is invalid or blocked!");
               }

               Account account = card.getAccount();

               if (amount.compareTo(FRAUD_THRESHOLD) >= 0) {
                    card.setIsBlocked(true);
                    session.merge(card);

                    AuditLog fraudAlert = new AuditLog(
                              "HIGH_VALUE_ATM_ATTEMPT",
                              "Blocked card " + cardNumber + " due to suspicious withdrawal of Rs. " + amount,
                              "CRITICAL_FRAUD_ALERT",
                              account.getUser());
                    session.persist(fraudAlert);
                    tx.commit();
                    System.err.println("FRAUD ALERT: Card auto-blocked and security log recorded!");
                    return;
               }

               if (amount.compareTo(card.getDailyLimit()) > 0) {
                    throw new IllegalStateException("Amount exceeds daily ATM card limit!");
               }

               // Deduct balance
               account.setBalance(account.getBalance().subtract(amount));
               session.merge(account);

               session.persist(new AuditLog("ATM_WITHDRAWAL", "Withdrew Rs. " + amount, "INFO", account.getUser()));
               tx.commit();
               System.out.println("ATM Withdrawal successful!");
          } catch (Exception e) {
               if (tx != null && tx.isActive())
                    tx.rollback();
               System.err.println("Withdrawal failed: " + e.getMessage());
          }
     }
}