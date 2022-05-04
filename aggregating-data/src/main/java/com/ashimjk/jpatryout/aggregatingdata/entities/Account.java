package com.ashimjk.jpatryout.aggregatingdata.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    private String reference;
    private String accountNumber;
    private BigDecimal accountLimit;
    private BigDecimal userLimit;

}
