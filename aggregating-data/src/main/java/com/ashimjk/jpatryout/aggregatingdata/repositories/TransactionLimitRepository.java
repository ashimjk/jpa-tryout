package com.ashimjk.jpatryout.aggregatingdata.repositories;

import com.ashimjk.jpatryout.aggregatingdata.entities.TransactionLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface TransactionLimitRepository extends JpaRepository<TransactionLimit, String> {

    @Query(value = "select totalAmount from (select sum(amount) as totalAmount from transaction_limit) tl", nativeQuery = true)
    DailyLimitSummary totalAmountOnly();

    @Query(value = "select totalAmount, dailyLimit, existingAmount from " +
            "(select sum(amt) as totalAmount from (select sum(amount) as amt from transaction_limit where account_number = :accountNumber and transaction_date = :transactionDate union select 0.0 from dual) ta) tl, " +
            "(select sum(user_limit) as dailyLimit from (select user_limit from account where account_number = :accountNumber union select 0.0 from dual) ul) a, " +
            "(select sum(amount) as existingAmount from (select amount from transaction_limit where reference = :reference union select 0.0 from dual) amt) ea",
            nativeQuery = true)
    DailyLimitSummary userDailyLimit(
            @Param("reference") String reference,
            @Param("accountNumber") String accountNumber,
            @Param("transactionDate") LocalDate transactionDate
    );

    interface DailyLimitSummary {

        BigDecimal getTotalAmount();

        BigDecimal getDailyLimit();

        BigDecimal getExistingAmount();

    }

}
