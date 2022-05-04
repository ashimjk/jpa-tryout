package com.ashimjk.jpatryout.aggregatingdata.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLimit {

    @Id
    private String reference;
    private String accountNumber;
    private LocalDate transactionDate;
    private BigDecimal amount;

}
