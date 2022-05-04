package com.ashimjk.jpatryout.aggregatingdata;

import com.ashimjk.jpatryout.aggregatingdata.repositories.TransactionLimitRepository;
import com.ashimjk.jpatryout.aggregatingdata.repositories.TransactionLimitRepository.DailyLimitSummary;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public record TransactionLimitService(TransactionLimitRepository limitRepository) {

    void execute() {
        print("Aggregate Total Amount");
        print("===========================================================");
        printSummary(limitRepository.totalAmountOnly());

        print("Empty Aggregate Total Amount, Daily Limit and Existing Amount");
        print("===========================================================");
        printSummary(limitRepository.userDailyLimit("ref", "acc", LocalDate.now()));

        print("Aggregate Total Amount, Daily Limit and Existing Amount");
        print("===========================================================");
        printSummary(limitRepository.userDailyLimit("ref1", "acc1", LocalDate.now()));
    }

    private void printSummary(DailyLimitSummary dailyLimitSummary) {
        if (dailyLimitSummary == null) {
            print("daily limit summary is null");
            print("");
            return;
        }

        print("Total Amount :", dailyLimitSummary.getTotalAmount());
        print("Daily Limit :", dailyLimitSummary.getDailyLimit());
        print("Existing Amount :", dailyLimitSummary.getExistingAmount());
        print("");
    }

    private void print(Object value) {
        System.out.println(value);
    }

    private void print(String message, Object value) {
        System.out.println(message + " " + value);
    }

}
