package com.paymentProject.controllers;

import com.paymentProject.dtos.request.TransactionDTO;
import com.paymentProject.entities.Transaction;
import com.paymentProject.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transactionRequest) throws Exception {
        Transaction transaction = transactionService.createTransaction(transactionRequest);
        return ResponseEntity.ok().body(transaction);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Transaction>> getAllUserTransactions(@PathVariable Long userId) throws Exception {
        List<Transaction> transactions = transactionService.getAllTransactionsByUserId(userId);
        return ResponseEntity.ok().body(transactions);
    }

    @GetMapping("/sender/{userId}")
    public ResponseEntity<List<Transaction>> getAllUserAsSenderTransactions(@PathVariable Long userId) throws Exception {
        List<Transaction> transactions = transactionService.getAllSendingTransactionsByUserId(userId);
        return ResponseEntity.ok().body(transactions);
    }

    @GetMapping("/receiver/{userId}")
    public ResponseEntity<List<Transaction>> getAllUserAsReceiverTransactions(@PathVariable Long userId) throws Exception {
        List<Transaction> transactions = transactionService.getAllReceivingTransactionsByUserId(userId);
        return ResponseEntity.ok().body(transactions);
    }
}
