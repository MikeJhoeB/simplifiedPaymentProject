package com.paymentProject.services;

import com.paymentProject.entities.Account;
import com.paymentProject.entities.User;
import com.paymentProject.exceptions.AccountException;
import com.paymentProject.repositories.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void createAccount(User user) {
        var account = Account.builder()
                .user(user)
                .balance(BigDecimal.TEN)
                .build();
        accountRepository.save(account);
    }

    public Account getAccountById(Long accountId) throws AccountException {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException("Account not found", HttpStatus.NOT_FOUND));
    }

    public Account getAccountByUserId(Long userId) throws AccountException {
        return accountRepository.findAccountByUserId(userId)
                .orElseThrow(() -> new AccountException("Account not found", HttpStatus.NOT_FOUND));
    }

    public Account getAccountByUser(User user) throws AccountException {
        return accountRepository.findAccountByUser(user)
                .orElseThrow(() -> new AccountException("Account not found", HttpStatus.NOT_FOUND));
    }

    public void debitValue(User user, BigDecimal value) throws AccountException {
        var account = getAccountByUser(user);
        account.setBalance(account.getBalance().subtract(value));
        accountRepository.save(account);
    }

    public void creditValue(User user, BigDecimal value) throws AccountException {
        var account = getAccountByUser(user);
        account.setBalance(account.getBalance().add(value));
        accountRepository.save(account);
    }
}
