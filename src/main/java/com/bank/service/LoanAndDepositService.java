package com.bank.service;

import com.bank.config.HibernateUtil;
import com.bank.entity.Account;
import com.bank.entity.EmiSchedule;
import com.bank.entity.Loan;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class LoanAndDepositService {

     public void approveLoanAndGenerateEmis(Long loanId, Long disbursalAccountId) {
          Transaction tx = null;
          try (Session session = HibernateUtil.getSessionFactory().openSession()) {
               tx = session.beginTransaction();

               Loan loan = session.get(Loan.class, loanId);
               Account account = session.get(Account.class, disbursalAccountId);

               if (loan == null || account == null) {
                    throw new IllegalArgumentException("Loan or Account not found!");
               }

               account.setBalance(account.getBalance().add(loan.getPrincipalAmount()));
               session.merge(account);

               double principal = loan.getPrincipalAmount().doubleValue();
               double monthlyRate = (loan.getInterestRate() / 12.0) / 100.0;
               int months = loan.getTenureMonths();

               double emiValue = (principal * monthlyRate * Math.pow(1 + monthlyRate, months))
                         / (Math.pow(1 + monthlyRate, months) - 1);
               BigDecimal monthlyEmi = BigDecimal.valueOf(emiValue).setScale(2, RoundingMode.HALF_UP);

               for (int i = 1; i <= months; i++) {
                    EmiSchedule schedule = new EmiSchedule(
                              i,
                              LocalDate.now().plusMonths(i),
                              monthlyEmi,
                              loan);
                    session.persist(schedule);
               }

               tx.commit();
               System.out.println("Loan Disbursed! Generated " + months + " EMI installments of Rs. " + monthlyEmi);
          } catch (Exception e) {
               if (tx != null && tx.isActive())
                    tx.rollback();
               e.printStackTrace();
          }
     }
}