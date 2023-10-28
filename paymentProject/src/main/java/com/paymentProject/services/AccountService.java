package com.paymentProject.services;

import com.paymentProject.entities.Account;
import com.paymentProject.entities.User;
import com.paymentProject.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void createAccount(User user) {
        var account = Account.builder()
                .user(user)
                .build();
        accountRepository.save(account);
    }

    public Account getAccountById(Long accountId) throws Exception {
        return Optional.of(accountRepository.findById(accountId)).get()
                .orElseThrow(() -> new Exception("Account not found"));
    }

    public Account getAccountByUser(User user) throws Exception {
        return Optional.of(accountRepository.findAccountByUser(user)).get()
                .orElseThrow(() -> new Exception("Account not found"));
    }
}