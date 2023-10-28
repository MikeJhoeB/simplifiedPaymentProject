package com.paymentProject.controllers;

import com.paymentProject.entities.Account;
import com.paymentProject.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Account> getAccountByUserId (@PathVariable Long userId) throws Exception {
        Account account = accountService.getAccountByUserId(userId);
        return ResponseEntity.ok().body(account);
    }
}
