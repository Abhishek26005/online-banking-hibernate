package com.bank.service;

import com.bank.config.HibernateUtil;
import com.bank.entity.Account;
import com.bank.entity.Transaction;
import com.bank.entity.User;
import org.hibernate.Session;
import java.math.BigDecimal;
import java.util.List;

public class BankingService {

     public void registerUserWithAccount(User user, Account initialAccount) {
          org.hibernate.Transaction tx = null;
          try (Session session = HibernateUtil.getSessionFactory().openSession()) {
               tx = session.beginTransaction();
               user.addAccount(initialAccount);
               session.persist(user);
               tx.commit();
               System.out.println(
                         "Customer registered successfully with Account: " + initialAccount.getAccountNumber());
          } catch (Exception e) {
               if (tx != null)
                    tx.rollback();
               e.printStackTrace();
          }
     }

     public void transferFunds(String fromAccNum, String toAccNum, BigDecimal amount, String remarks) {
          org.hibernate.Transaction tx = null;
          try (Session session = HibernateUtil.getSessionFactory().openSession()) {
               tx = session.beginTransaction();

               // Fetch both accounts using HQL
               Account source = session.createQuery("FROM Account WHERE accountNumber = :accNo", Account.class)
                         .setParameter("accNo", fromAccNum)
                         .uniqueResult();

               Account target = session.createQuery("FROM Account WHERE accountNumber = :accNo", Account.class)
                         .setParameter("accNo", toAccNum)
                         .uniqueResult();

               if (source == null || target == null) {
                    throw new IllegalArgumentException("Invalid source or destination account number!");
               }

               if (source.getBalance().compareTo(amount) < 0) {
                    throw new IllegalStateException("Insufficient balance in account: " + fromAccNum);
               }

               source.setBalance(source.getBalance().subtract(amount));
               target.setBalance(target.getBalance().add(amount));

               session.merge(source);
               session.merge(target);

               Transaction bankTx = new Transaction("TRANSFER", amount, source, target, remarks);
               session.persist(bankTx);

               tx.commit();
               System.out.println("Transfer Successful! Ref ID: " + bankTx.getReferenceNumber());

          } catch (Exception e) {
               if (tx != null && tx.isActive()) {
                    tx.rollback();
                    System.err.println("Transaction Rolled Back! Reason: " + e.getMessage());
               }
          }
     }

     public List<Transaction> getAccountStatement(String accountNumber) {
          try (Session session = HibernateUtil.getSessionFactory().openSession()) {
               String hql = """
                         FROM Transaction t
                         WHERE t.sourceAccount.accountNumber = :accNo
                            OR t.targetAccount.accountNumber = :accNo
                         ORDER BY t.timestamp DESC
                         """;
               return session.createQuery(hql, Transaction.class)
                         .setParameter("accNo", accountNumber)
                         .setMaxResults(10)
                         .list();
          }
     }
}