package com.ashimjk.jpatryout.aggregatingdata;

import com.ashimjk.jpatryout.aggregatingdata.entities.Account;
import com.ashimjk.jpatryout.aggregatingdata.entities.TransactionLimit;
import com.ashimjk.jpatryout.aggregatingdata.repositories.AccountRepository;
import com.ashimjk.jpatryout.aggregatingdata.repositories.TransactionLimitRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class AggregatingDataApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AggregatingDataApp.class, args);
        context.close();
    }

    @Bean
    CommandLineRunner initData(
            AccountRepository accountRepository,
            TransactionLimitRepository limitRepository,
            TransactionLimitService limitService
    ) {
        return args -> {
            accountRepository.save(new Account("ref1", "acc1", BigDecimal.ONE, BigDecimal.ONE));

            limitRepository.save(new TransactionLimit("ref1", "acc1", LocalDate.now(), BigDecimal.TEN));
            limitRepository.save(new TransactionLimit("ref2", "acc1", LocalDate.now(), BigDecimal.TEN));
            limitRepository.save(new TransactionLimit("ref3", "acc1", LocalDate.now(), BigDecimal.TEN));

            limitService.execute();
        };
    }

}
