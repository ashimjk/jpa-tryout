package com.ashimjk.jpatryout.aggregatingdata.repositories;

import com.ashimjk.jpatryout.aggregatingdata.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
}
