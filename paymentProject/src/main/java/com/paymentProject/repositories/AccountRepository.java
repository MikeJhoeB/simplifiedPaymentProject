package com.paymentProject.repositories;

import com.paymentProject.entities.Account;
import com.paymentProject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByUser(User user);
}
