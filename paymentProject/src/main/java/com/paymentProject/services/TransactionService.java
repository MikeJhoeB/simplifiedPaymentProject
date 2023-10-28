package com.paymentProject.services;

import com.paymentProject.entities.Transaction;
import com.paymentProject.entities.User;
import com.paymentProject.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createTransaction(User sendingUser, User receivingUser, BigDecimal value) {
        var transaction = Transaction.builder()
                .sendingUser(sendingUser)
                .receivingUser(receivingUser)
                .value(value)
                .build();
        return transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(Long transactionId) throws Exception {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new Exception("Transaction not found"));
    }

    public List<Transaction> getAllTransactionsByUser(User user) throws Exception {
        return transactionRepository.findAllBySendingUserOrReceivingUser(user, user)
                .orElseThrow(() -> new Exception("Transactions not found"));
    }

    public List<Transaction> getAllSendingTransactionsByUser(User user) throws Exception {
        return transactionRepository.findAllBySendingUser(user)
                .orElseThrow(() -> new Exception("Transactions not found"));
    }

    public List<Transaction> getAllReceivingTransactionsByUser(User user) throws Exception {
        return transactionRepository.findAllByReceivingUser(user)
                .orElseThrow(() -> new Exception("Transactions not found"));
    }
}
