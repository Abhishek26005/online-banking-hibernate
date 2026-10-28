# 🏦 Enterprise Online Banking System (Hibernate ORM)

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-6.4.4-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![Status](https://img.shields.io/badge/Project-MCA%20Major%20Project-success?style=for-the-badge)

An enterprise-grade, **ACID-compliant Core Banking System** built using **Java**, **Hibernate ORM (Jakarta Persistence / JPA)**, and **MySQL**. Designed as a 4-module collaborative architecture covering Customer e-KYC Onboarding, Atomic Fund Transfers with Optimistic Locking, Automated Loan & EMI Scheduling, and Real-Time Card Fraud Detection.

---

## 📌 Key Architectural Highlights

* **Concurrency & Race-Condition Protection (`@Version`):** Implements Hibernate Optimistic Locking on bank accounts to prevent dirty reads and double-spending during simultaneous ATM or online transfers.
* **ACID Transaction Engine:** All debits, credits, and audit ledger entries execute inside atomic Hibernate transaction boundaries (`beginTransaction()` $\rightarrow$ `commit()` / `rollback()`).
* **High-Precision Monetary Math:** Uses `java.math.BigDecimal` mapped to SQL `DECIMAL(15,2)` across all financial entities to eliminate floating-point rounding anomalies.
* **Automated Reducing-Balance EMI Scheduler:** Dynamically calculates monthly loan installments and batch-inserts repayment schedules upon loan approval.
* **Rule-Based Fraud Guard:** Automatically flags high-value withdrawals exceeding safety thresholds, blocks compromised debit cards, and records critical security alerts.

---

## 👥 4-Module Team Architecture

The system is architected into **4 independent domain modules** across **10 relational entities**:

| Module | Domain Responsibility | Entities Owned | Core Features |
| :--- | :--- | :--- | :--- |
| **Module 1** | **Customer Onboarding, e-KYC & Multi-Account Engine** | `User`, `KycDocument`, `Account` | `@OneToOne` PAN/Aadhaar KYC verification workflow, Savings/Current account provisioning, Account freeze/unfreeze controls, Role-Based Access (`CUSTOMER`, `MANAGER`, `ADMIN`). |
| **Module 2** | **Core Payment Gateway, Beneficiaries & ACID Ledger** | `Transaction`, `Beneficiary` | `@Version` Optimistic Locking, NEFT/RTGS/IMPS fund transfers, Payee limit enforcement, Double-entry transaction history & mini-statement HQL queries. |
| **Module 3** | **Loan Origination, EMI Scheduler & Fixed Deposits** | `Loan`, `EmiSchedule`, `FixedDeposit` | Automated reducing-balance EMI calculation, Batch generation of monthly installment schedules, FD compound interest & maturity tracking. |
| **Module 4** | **Card Management, Fraud Detection & Security Audit** | `DebitCard`, `AuditLog` | Virtual/Physical Debit Card issuance, SHA-256 PIN hashing, Daily ATM limit enforcement, Automated fraud flagging & card hotlisting. |

---

## 🗂️ Database ER Diagram

GitHub natively renders the Mermaid Entity-Relationship diagram below:

```mermaid
erDiagram
    USER ||--o| KYC_DOCUMENT : "submits (1:1)"
    USER ||--o{ ACCOUNT : "holds (1:N)"
    USER ||--o{ BENEFICIARY : "registers (1:N)"
    USER ||--o{ LOAN : "applies_for (1:N)"
    USER ||--o{ AUDIT_LOG : "triggers (1:N)"

    ACCOUNT ||--o| DEBIT_CARD : "linked_to (1:1)"
    ACCOUNT ||--o{ FIXED_DEPOSIT : "funds (1:N)"
    ACCOUNT ||--o{ TRANSACTION : "source_or_target (1:N)"

    LOAN ||--o{ EMI_SCHEDULE : "generates (1:N)"

    USER {
        Long userId PK
        String fullName
        String email UK
        String phone UK
        String passwordHash
        String role
        LocalDateTime createdAt
    }

    KYC_DOCUMENT {
        Long kycId PK
        String panNumber UK
        String aadhaarNumber UK
        String residentialAddress
        String verificationStatus
        Long user_id FK
    }

    ACCOUNT {
        Long accountId PK
        String accountNumber UK
        String accountType
        BigDecimal balance
        String status
        Long version
        Long user_id FK
    }

    TRANSACTION {
        Long transactionId PK
        String referenceNumber UK
        String transactionType
        BigDecimal amount
        LocalDateTime timestamp
        Long source_account_id FK
        Long target_account_id FK
    }

    BENEFICIARY {
        Long beneficiaryId PK
        String beneficiaryName
        String accountNumber
        String ifscCode
        BigDecimal maxTransferLimit
        Long user_id FK
    }

    LOAN {
        Long loanId PK
        String loanType
        BigDecimal principalAmount
        Double interestRate
        Integer tenureMonths
        String status
        Long user_id FK
    }

    EMI_SCHEDULE {
        Long emiId PK
        Integer installmentNumber
        LocalDate dueDate
        BigDecimal emiAmount
        String paymentStatus
        Long loan_id FK
    }

    FIXED_DEPOSIT {
        Long fdId PK
        BigDecimal principalAmount
        Double interestRate
        Integer durationMonths
        BigDecimal maturityAmount
        LocalDate maturityDate
        Long account_id FK
    }

    DEBIT_CARD {
        Long cardId PK
        String cardNumber UK
        LocalDate expiryDate
        String pinHash
        BigDecimal dailyLimit
        Boolean isBlocked
        Long account_id FK
    }

    AUDIT_LOG {
        Long logId PK
        String actionType
        String description
        String severity
        LocalDateTime timestamp
        Long user_id FK
    }
